FROM ubuntu:hirsute

ARG BAZEL_VERSION=4.2.2

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y xz-utils python git apt-transport-https curl gnupg openjdk-17-jdk && \
    curl -fsSL https://bazel.build/bazel-release.pub.gpg | gpg --dearmor > bazel.gpg && mv bazel.gpg /etc/apt/trusted.gpg.d/ && \
    echo "deb [arch=amd64] https://storage.googleapis.com/bazel-apt stable jdk1.8" | tee /etc/apt/sources.list.d/bazel.list && \
    apt-get update && apt-get install -y bazel=$BAZEL_VERSION && \
    curl -sL https://deb.nodesource.com/setup_16.x | bash - && apt-get update && apt-get install -y nodejs && \
    npm install -g yarn && \
    curl -sL https://github.com/bazelbuild/buildtools/releases/download/4.2.5/buildifier-linux-amd64 -o /usr/bin/buildifier && chmod +x /usr/bin/buildifier && \
    echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && \
    curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg add - && \
    apt-get update && apt-get install -y google-cloud-sdk

ARG USERNAME=vscode

RUN useradd -ms /bin/bash $USERNAME

VOLUME /home/$USERNAME/.config/gcloud

USER $USERNAME

RUN mkdir -p /home/$USERNAME/.config/gcloud

RUN bazel --version
