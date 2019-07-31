load("@io_bazel_rules_docker//container:providers.bzl", "PushInfo")

def _run_deployment_impl(ctx):
    registry = ctx.attr.image_push[PushInfo].registry
    repository = ctx.attr.image_push[PushInfo].repository
    image = "%s/%s" % (registry, repository)

    deploy_deps_string = "\n".join([
        label[DefaultInfo].files_to_run.executable.short_path + " " + name
        for label, name in ctx.attr.deps.items()
    ])

    env_vars_string = ",".join(["%s=%s" % (k, v) for k, v in ctx.attr.env.items()])

    service_name_prefix = ctx.label.name.replace("_", "-")
    if service_name_prefix.startswith("deploy-"):
        service_name_prefix = service_name_prefix[len("deploy-"):][0:30]

    ctx.actions.expand_template(
        template = ctx.file._deploy_template,
        output = ctx.outputs.deploy_script,
        substitutions = {
            "{DEPLOY_BIN}": ctx.attr._deploy[DefaultInfo].files_to_run.executable.short_path,
            "{DEPLOY_DEPS}": deploy_deps_string,
            "{PUSH_IMAGE}": ctx.attr.image_push[DefaultInfo].files_to_run.executable.short_path,
            "{IMAGE}": image,
            "{ENV_VARS}": env_vars_string,
            "{CONCURRENCY}": str(ctx.attr.concurrency),
            "{REGION}": ctx.attr.region,
            "{MEMORY}": ctx.attr.memory,
            "{PROJECT_ID}": ctx.attr.project_id,
            "{SERVICE_NAME_PREFIX}": service_name_prefix,
            "{SERVICE_ACCOUNT_FILE}": ctx.file.service_account_file.short_path,
        },
        is_executable = False,
    )

    runfiles = ctx.runfiles(
        files = [
            ctx.file.service_account_file,
            ctx.attr.image_push[PushInfo].digest,
        ] + ctx.attr.image_push[DefaultInfo].files.to_list(),
    )

    runfiles = runfiles.merge(ctx.attr.image_push[DefaultInfo].default_runfiles)

    runfiles = runfiles.merge(ctx.attr._deploy[DefaultInfo].default_runfiles)

    for label in ctx.attr.deps:
        runfiles = runfiles.merge(label[DefaultInfo].default_runfiles)

    return [
        DefaultInfo(
            executable = ctx.outputs.deploy_script,
            runfiles = runfiles,
        ),
    ]

run_deployment = rule(
    implementation = _run_deployment_impl,
    attrs = {
        "env": attr.string_dict(),
        "image_push": attr.label(
            mandatory = True,
            providers = [PushInfo, DefaultInfo],
        ),
        "concurrency": attr.int(
            default = 80,
        ),
        "memory": attr.string(
            default = "512Mi",
        ),
        "region": attr.string(
            default = "europe-west1",
        ),
        "project_id": attr.string(
            mandatory = True,
        ),
        "deps": attr.label_keyed_string_dict(),
        "service_account_file": attr.label(
            allow_single_file = True,
            mandatory = True,
        ),
        "_deploy_template": attr.label(
            default = Label("//run:deploy.sh"),
            allow_single_file = True,
        ),
        "_deploy": attr.label(
            default = Label("//run/src/main/java/com/github/iljakroonen/rules/run:Deploy"),
        ),
    },
    outputs = {
        "deploy_script": "%{name}.sh",
    },
    executable = True,
)
