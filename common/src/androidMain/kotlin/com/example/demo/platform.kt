package com.example.demo

actual fun getPlatformName(): String {
    return "Android"
}

actual typealias JavaSerializable = java.io.Serializable