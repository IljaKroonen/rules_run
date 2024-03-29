#!/usr/bin/env bash

set -e

credentials="{GOOGLE_APPLICATION_CREDENTIALS}"

if [[ ! -f "$credentials" ]]
then
    credentials="$HOME/.config/gcloud/application_default_credentials.json"
    if [[ ! -f "$credentials" ]]
    then
        printf "Credentials file could not be resolved from passed service_account or from GOOGLE_APPLICATION_CREDENTIALS env var / gcloud save location\n"
        exit 1
    fi
fi

files_to_be_deleted=""
function finish {
    rm -rf ${files_to_be_deleted}
}

trap finish exit

env_vars_string="{ENV_VARS}"

for dep_line in "{DEPLOY_DEPS}"
do
    if [[ -z "${dep_line}" ]]
    then
        break
    fi
    exec=$(printf "${dep_line}" | cut -d" " -f1)
    dep_name=$(printf "${dep_line}" | cut -d" " -f2)

    service_url_file=$(mktemp)
    files_to_be_deleted="${files_to_be_deleted} ${service_url_file}"
    ./"${exec}" "${service_url_file}"
    if [[ -z "${env_vars_string}" ]]
    then
        env_vars_string="SERVICE_${dep_name}_URI=$(cat ${service_url_file})"
    else
        env_vars_string="${env_vars_string},SERVICE_${dep_name}_URI=$(cat ${service_url_file})"
    fi
done

push="{PUSH_IMAGE}"

full_image="{IMAGE}@$(cat ${push}.digest)"

service_digest="$(printf "${full_image}_${env_vars_string}_{MEMORY}_{CONCURRENCY}" | sha256sum | cut -d" " -f1)"
service_name_prefix="{SERVICE_NAME_PREFIX}"
if [[ "{GENERATE_UNIQUE_NAME}" = "True" ]]
then
    service_name="${service_name_prefix}-${service_digest:0:32}" # Service names must be 63 characters or less and start with a letter
else
    service_name="${service_name_prefix}"
fi

printf "Publishing service with image ${full_image} as service ${service_name}\n"
./${push}

url_file=$(mktemp)
files_to_be_deleted="${files_to_be_deleted} ${url_file}"
if ! [[ -z "${1}" ]]
then
    url_file="${1}"
fi

GOOGLE_APPLICATION_CREDENTIALS=${credentials} "{DEPLOY_BIN}" \
    --region "{REGION}"\
    --projectId "{PROJECT_ID}" \
    --serviceName ${service_name} \
    --image ${full_image} \
    --env "${env_vars_string}" \
    --memory "{MEMORY}" \
    --concurrency "{CONCURRENCY}" \
    --urlFile ${url_file}

printf "Service ${service_name} available at $(cat ${url_file})\n"
