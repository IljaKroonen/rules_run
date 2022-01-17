# Cloud Run deployment rules for Bazel

## Setup

These rules require [rules_docker](https://github.com/bazelbuild/rules_docker).

In addition to rules_docker, add the following to your WORKSPACE file to add the external repositories:
```
RULES_RUN_TAG="commit_hash_or_tag"

http_archive(
    name = "rules_run",
    strip_prefix = "rules_run-%s" % RULES_RUN_TAG,
    url = "https://github.com/iljakroonen/rules_run/archive/%s.zip" % RULES_RUN_TAG,
)

load("@rules_run//repositories:repositories.bzl", "run_repositories")

run_repositories()
```

## Usage

```
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:push.bzl", "container_push")
load("@com_github_iljakroonen_rules_run//run:run.bzl", "run_deployment")

java_image(
    name = "dep",
    srcs = ["Dep.java"],
    main_class = "example.Dep",
    deps = [
        "@maven//:io_javalin_javalin",
    ],
)

container_push(
    name = "push_dep",
    format = "Docker",
    image = ":dep",
    registry = "eu.gcr.io",
    repository = "rules-run-dev/example_dep",
)

run_deployment(
    name = "deploy_dep",
    image_push = ":push_dep",
    project_id = "rules-run-dev",
)

java_image(
    name = "example",
    srcs = ["Main.java"],
    main_class = "example.Main",
    deps = [
        "@maven//:io_javalin_javalin",
    ],
)

container_push(
    name = "push_example",
    format = "Docker",
    image = ":example",
    registry = "eu.gcr.io",
    repository = "rules-run-dev/example",
)

run_deployment(
    name = "deploy_example",
    image_push = ":push_example",
    project_id = "rules-run-dev",
    deps = {
        ":deploy_dep": "DEP",
    },
)
```

The app can then be deployed by running `bazel run //:deploy_example`.

## Additional notes

- this is a random side project, use at your own risk
- all deployments are automatically made public
- the rules assume that the `container_push` rules pushes the container to the same project as the `run_deployment`
target project
