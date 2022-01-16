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
