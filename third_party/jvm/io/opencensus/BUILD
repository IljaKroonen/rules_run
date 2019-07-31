licenses(["notice"])
java_library(
    name = "opencensus_api",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runio/opencensus/opencensus_api"
    ],
    runtime_deps = [
        "//third_party/jvm/io/grpc:grpc_context"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "opencensus_contrib_http_util",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runio/opencensus/opencensus_contrib_http_util"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/guava:guava",
        ":opencensus_api"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


