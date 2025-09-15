package com.example.darkscreen.timer

sealed interface TimerState {
    data object Idle : TimerState
    data class Running(val remainingMs: Long) : TimerState
    data class Paused(val remainingMs: Long) : TimerState
    data object Finished : TimerState

}