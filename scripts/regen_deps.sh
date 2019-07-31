#!/usr/bin/env bash

set -e

MSYS_NO_PATHCONV=1 bazel run "//third_party:bazel_deps" generate -- \
    -r $(bazel info workspace) \
    -d third_party/dependencies.yaml \
    -s third_party/jvm/workspace.bzl
