java_binary(
    name = "Deploy",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//third_party/jvm/com/fasterxml/jackson/core:jackson_core",
        "//third_party/jvm/com/fasterxml/jackson/core:jackson_databind",
        "//third_party/jvm/com/google/auth:google_auth_library_credentials",
        "//third_party/jvm/com/google/auth:google_auth_library_oauth2_http",
        "//third_party/jvm/com/google/http_client:google_http_client",
        "//third_party/jvm/com/google/http_client:google_http_client_jackson2",
        "//third_party/jvm/net/sourceforge/argparse4j",
    ],
)
