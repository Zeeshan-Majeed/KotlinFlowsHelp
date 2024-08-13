package com.fcmx.helper.kotlinflowshelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    /**
     * Live data which always needs initial value
     * when configuration changes occurred it collects last emitted value
     * it was old approach also used in JAVA
     */
    val liveData = MutableLiveData("Live Data Ready")

    /**
     * State FLow it always needs initial value
     * work as live data
     * when configuration changes occurred it collects last emitted value
     */
    private val _stateFlow = MutableStateFlow("State Flow Ready")
    val stateFlow = _stateFlow.asStateFlow()

    /**
     * Shared flow always need a collector
     * if there is no collector it will not emit anything
     * don't need initial value
     * it collects last emitted value and forgot initial values
     * and collect only once.
     * when configuration changes it will not collect last emitted value,
     * until we emit again
     */

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    /**
     * Simple FLow can collect all values as state flow
     * but forgot after collection, unable to hold last collected value
     */

    val simpleFLow = flow {
        emit("Starting simple flow...")
        delay(1000)
        emit("Simple flow triggered")
    }


    fun triggerLiveData() = viewModelScope.launch {
        liveData.postValue("Live data triggered")
    }

    fun triggerStateFlow() = viewModelScope.launch {
        _stateFlow.value = "Emitting state flow data..."
        delay(1000)
        _stateFlow.value = "State flow triggered"
    }

    fun triggerSharedFlow() = viewModelScope.launch {
        _sharedFlow.emit("Emitting shared flow data...")
        delay(1000)
        _sharedFlow.emit("Shared flow triggered")
    }

}