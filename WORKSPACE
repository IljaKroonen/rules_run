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
    sha256 = "df13123c44b4a4ff2c2f337b906763879d94871d16411bf82dcfeba892b58607",
    strip_prefix = "rules_docker-0.13.0",
    urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.13.0/rules_docker-v0.13.0.tar.gz"],
)

load("//repositories:repositories.bzl", "run_repositories")

run_repositories()
