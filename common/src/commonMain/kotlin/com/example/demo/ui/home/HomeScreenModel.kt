package com.example.demo.ui.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.demo.manager.ClickManager
import org.koin.core.annotation.Factory

@Factory
class HomeScreenModel(
    private val clickManager: ClickManager
) : ScreenModel {
    val counter = clickManager.clickCount.stateIn(screenModelScope, SharingStarted.Eagerly, null)

    fun incrementCounter() {
        screenModelScope.launch {
            clickManager.incrementClickCount()
        }
    }

    fun resetCounter() {
        screenModelScope.launch {
            clickManager.resetClickCount()
        }
    }
}
