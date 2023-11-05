package com.example.demo

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.demo.ui.home.HomeScreen
import com.example.demo.ui.theme.ExampleTheme
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        ExampleTheme {
            Navigator(HomeScreen())
        }
    }
}