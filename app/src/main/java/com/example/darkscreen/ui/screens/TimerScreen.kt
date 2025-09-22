package com.example.darkscreen.ui.screens

import android.app.Activity
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darkscreen.timer.TimerState
import com.darkscreen.timer.TimerViewModel
import com.darkscreen.ui.theme.*
import com.example.darkscreen.timer.TimerState
import com.example.darkscreen.timer.TimerViewModel

@Composable
fun TimerScreen(minutes: Int, onBack: () -> Unit, vm: TimerViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val ctx = LocalContext.current
    val activity = ctx as Activity

    LaunchedEffect(minutes) { vm.start(minutes)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    DisposedEffect(Unit) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    DisposableEffect(state) {
        val orignial = activity.window.attributes.screenBrightness
        val lp = activity.window.attributes
        lp.screenBrightness = when (state) {
            is TimerState.Running, is TimerState.Paused -> 0.02f
            else -> WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        }
        activity.window.attributes = lp
        onDispose {
            val restore = activity.window.attributes
            restore.screenBrightness = orignial
            activity.window.attributes = restore
        }
    }

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val remaining = when (val s = state) {
                is TimerState.Running -> s.remainingMs
                is TimerState.Paused -> s.remainingMs
                is TimerState.Finished -> 0L
                is TimerState.Idle -> minutes * 60_000L
                else -> {}
            }
            val mm = (remaining / 60_000).toInt().toString().padStart(2, '0')
            val ss = ((remaining / 1000) % 60).toInt().toString().padStart(2, '0')

            Text(
                text = "$mm:$ss",
                color = TextPrimary,
                style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Light
            ),

            Spacer(Modifier.height(24.dp)),

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                when (state) {
                    is TimerState.Running -> {
                        Button(
                            onClick = { vm.pause() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentDim)
                        ) { Text("Pause") }
                        OutlinedButton(onClick = { vm.resume(); onBack() }) { Text("Stop") }
                    }
                    is TimerState.Paused -> {
                        Button(
                            onClick = { vm.resume() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentDim)
                        ) { Text("Resume") }
                        OutlinedButton(onClick = { vm.reset(); onBack() }) { Text("Stop") }
                    }
                    is TimerState.Finished -> {
                        Button(
                            onClick = { onBack(); vm.reset() },
                            colors = ButtonDefaults.buttonColors(containerColor = Accent)
                        ) { Text("Done") }
                    }
                    else -> {
                        Button(onClick = { vm.start(minutes) }) { Text("Start") }
                    }
                }
            })
        }
    }
}
