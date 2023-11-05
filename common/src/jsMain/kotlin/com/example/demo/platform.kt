package com.example.demo

actual fun getPlatformName(): String {
    return "Web"
}

actual interface JavaSerializable
