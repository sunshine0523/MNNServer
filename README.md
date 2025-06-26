# MNN Server

[简体中文](./README_zh-CN.md) 

A third-party MNN server supporting external calls, vector model, and speech recognition model features.

# News 🔥

- [2024/06/26 v0.0.6] Offline text-to-speech TTS models are supported, and the TTS engine supports Sherpa and Bert-VITS2
- [2025/06/03 v0.0.3] Support offline voice recognition models


# Screenshots

<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="./img/img1.jpg" style="width: 30%">
  <img src="./img/img2.jpg" style="width: 30%"> 
  <img src="./img/img3.jpg" style="width: 30%">
</div>

# Introduction

MNN Server is an Android third-party MNN server that can provide external programs with MNN model calls in the style of the OpenAI API interface. Features of MNN Server include:

- Supports external calls, can be used as a model provider for Android or PC applications
- Support Embedding Model call

# Usage

- Download the MNN Server app
- Start the service
- Download the model in the model list and start it
- If you need to call an external device, enable the Expose Service Port

# Acknowledgement

We would like to express our gratitude to the following projects:
- **[MNN](https://github.com/alibaba/MNN)**: Without MNN, this project would not exist.
- **[mnn-asr](https://github.com/wangzhaode/mnn-asr)**

# License

[LICENSE](./LICENSE)