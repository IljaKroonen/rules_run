workspace(
    name = "rules_run",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")

http_jar(
    name = "bazel_deps",
    sha256 = "fd27227dd47bd3fb0e1bca912167d20e4655845a2af4f162de682ec6a3babc83",
    url = "https://github.com/IljaKroonen/artifacts/raw/master/parseproject_deploy_1af8921d52f053fad575f26762533a3823b4a847.jar",
)

http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "87fc6a2b128147a0a3039a2fd0b53cc1f2ed5adb8716f50756544a572999ae9a",
    strip_prefix = "rules_docker-0.8.1",
    urls = ["https://github.com/bazelbuild/rules_docker/archive/v0.8.1.tar.gz"],
)

load("//repositories:repositories.bzl", "run_repositories")

run_repositories()
