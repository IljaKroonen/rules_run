licenses(["notice"])
java_library(
    name = "google_auth_library_credentials",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/auth/google_auth_library_credentials"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "google_auth_library_oauth2_http",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/auth/google_auth_library_oauth2_http"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/auto/value:auto_value_annotations",
        "//third_party/jvm/com/google/code/findbugs:jsr305",
        "//third_party/jvm/com/google/guava:guava",
        "//third_party/jvm/com/google/http_client:google_http_client",
        "//third_party/jvm/com/google/http_client:google_http_client_gson",
        ":google_auth_library_credentials"
    ],
    visibility = [
        "//visibility:public"
    ]
)


