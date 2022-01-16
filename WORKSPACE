workspace(
    name = "com_github_iljakroonen_rules_run",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_jar")

http_jar(
    name = "bazel_deps",
    sha256 = "fd27227dd47bd3fb0e1bca912167d20e4655845a2af4f162de682ec6a3babc83",
    url = "https://github.com/IljaKroonen/artifacts/raw/master/parseproject_deploy_1af8921d52f053fad575f26762533a3823b4a847.jar",
)

http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "59536e6ae64359b716ba9c46c39183403b01eabfbd57578e84398b4829ca499a",
    strip_prefix = "rules_docker-0.22.0",
    urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.22.0/rules_docker-v0.22.0.tar.gz"],
)

load("//repositories:repositories.bzl", "run_repositories")

run_repositories()
