licenses(["notice"])
java_library(
    name = "google_http_client",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/http_client/google_http_client"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/code/findbugs:jsr305",
        "//third_party/jvm/com/google/guava:guava",
        "//third_party/jvm/com/google/j2objc:j2objc_annotations",
        "//third_party/jvm/io/opencensus:opencensus_api",
        "//third_party/jvm/io/opencensus:opencensus_contrib_http_util",
        "//third_party/jvm/org/apache/httpcomponents:httpclient",
        "//third_party/jvm/org/apache/httpcomponents:httpcore"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "google_http_client_gson",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/http_client/google_http_client_gson"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/code/gson:gson",
        ":google_http_client"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "google_http_client_jackson2",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/http_client/google_http_client_jackson2"
    ],
    runtime_deps = [
        "//third_party/jvm/com/fasterxml/jackson/core:jackson_core",
        ":google_http_client"
    ],
    visibility = [
        "//visibility:public"
    ]
)


