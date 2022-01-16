licenses(["notice"])
java_library(
    name = "failureaccess",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/guava/failureaccess"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "guava",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/guava/guava"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/code/findbugs:jsr305",
        "//third_party/jvm/com/google/errorprone:error_prone_annotations",
        "//third_party/jvm/com/google/j2objc:j2objc_annotations",
        "//third_party/jvm/org/checkerframework:checker_compat_qual",
        "//third_party/jvm/org/checkerframework:checker_qual",
        ":failureaccess",
        ":listenablefuture"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "listenablefuture",
    exports = [
        "//external:jar/com_google_github_iljakroonen_rules_runcom/google/guava/listenablefuture"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


