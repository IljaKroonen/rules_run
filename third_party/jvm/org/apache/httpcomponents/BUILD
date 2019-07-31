licenses(["notice"])
java_library(
    name = "httpclient",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runorg/apache/httpcomponents/httpclient"
    ],
    runtime_deps = [
        "//third_party/jvm/commons_codec:commons_codec",
        "//third_party/jvm/commons_logging:commons_logging",
        ":httpcore"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "httpcore",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runorg/apache/httpcomponents/httpcore"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


