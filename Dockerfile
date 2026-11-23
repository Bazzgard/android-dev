FROM node:24-bookworm-slim

ARG ANDROID_API_LEVEL=36
ARG ANDROID_BUILD_TOOLS=35.0.0
ARG CODEX_VERSION=latest

ENV ANDROID_HOME=/opt/android-sdk \
    JAVA_HOME=/opt/java
ENV PATH="${JAVA_HOME}/bin:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:/opt/gradle/bin:${PATH}"

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates curl git openjdk-17-jdk-headless ripgrep unzip \
    && ln -s "$(dirname "$(dirname "$(readlink -f "$(command -v javac)")")")" "${JAVA_HOME}" \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://dl.google.com/android/repository/commandlinetools-linux-15859902_latest.zip -o /tmp/android-tools.zip \
    && echo '4e4c464f145a7512b57d088ac6c278c03c9eea610886b35a5e0804e74eedf583  /tmp/android-tools.zip' | sha256sum -c - \
    && mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && unzip -q /tmp/android-tools.zip -d /tmp/android-tools \
    && mv /tmp/android-tools/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm -rf /tmp/android-tools /tmp/android-tools.zip \
    && yes | sdkmanager --licenses \
    && sdkmanager --install "platform-tools" "platforms;android-${ANDROID_API_LEVEL}" "build-tools;${ANDROID_BUILD_TOOLS}"

RUN curl -fsSL https://services.gradle.org/distributions/gradle-8.14.8-bin.zip -o /tmp/gradle.zip \
    && echo '94da89692fc3ca6d2d0ce6bb48548762ea0f1897ba444286f5c7f3759c7af651  /tmp/gradle.zip' | sha256sum -c - \
    && unzip -q /tmp/gradle.zip -d /opt \
    && mv /opt/gradle-8.14.8 /opt/gradle \
    && rm /tmp/gradle.zip

ENV CODEX_HOME=/home/node/.codex \
    GRADLE_USER_HOME=/home/node/.gradle

RUN npm install --global "@openai/codex@${CODEX_VERSION}" \
    && npm cache clean --force \
    && install -d -o node -g node /workspace "${CODEX_HOME}" "${GRADLE_USER_HOME}"

USER node
WORKDIR /workspace

RUN javac -version \
    && gradle --version \
    && sdkmanager --version \
    && adb version \
    && codex --version

CMD ["bash"]
