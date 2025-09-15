package com.example.darkscreen.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val fmt = DateTimeFormatter.ofPattern("HH:mm")

fun clockFlow(): Flow<String> = flow {
    while (true) {
        emit(LocalTime.now().format(fmt))
        delay(1000L)
    }
}