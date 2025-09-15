package com.example.darkscreen.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class TimerViewModel: ViewModel() {
    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    val state: StateFlow<TimerState> = _state

    private var totalMs: Long = 0L
    private var remainingMs: Long = 0L
    private var ticker: Job? = null

    fun startTimer(totalSeconds: Int) {
        totalMs = minutes * 60_000L
        remainingMs = totalMs
        _state.value = TimerState.Running(remainingMs)
        tick()
    }

    fun resume() {
        if (_state.value is TimerState.Paused) {
            _state.value = TimerState.Running(remainingMs)
            tick()
        }
    }

    fun pause() {
        ticker?.cancel()
        _state.value = TimerState.Paused(remainingMs)
    }

    fun reset() {
        ticker?.cancel()
        remainingMs = totalMs
        _state.value = TimerState.Idle
    }

    private fun tick() {
        ticker?.cancel()
        ticker = viewModelScope.launch {
            var last = System.currentTimeMillis()
            while (remainingMs > 0) {
                delay(100L)
                val now = System.currentTimeMillis()
                val dt = now - last
                last = now
                remainingMs = max(0L, remainingMs - dt)
                if (remainingMs == 0L) {
                    _state.value = TimerState.Finished
                    break
                } else {
                    _state.value = TimerState.Running(remainingMs)
                }
            }
        }
    }
}