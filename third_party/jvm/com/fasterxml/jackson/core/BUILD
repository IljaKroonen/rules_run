licenses(["notice"])
java_library(
    name = "jackson_annotations",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/fasterxml/jackson/core/jackson_annotations"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "jackson_core",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/fasterxml/jackson/core/jackson_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "jackson_databind",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/fasterxml/jackson/core/jackson_databind"
    ],
    runtime_deps = [
        ":jackson_annotations",
        ":jackson_core"
    ],
    visibility = [
        "//visibility:public"
    ]
)


