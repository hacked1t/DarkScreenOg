package com.example.darkscreen.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.darkscreen.domain.Preset
import com.darkscreen.ui.theme.*
import com.example.darkscreen.domain.Preset

@Composable
fun HomeScreen(onStart: (Int) -> Unit) {
    val presets = remember {
        listOf(
            Preset("p10", "10 min", 10),
            Preset("p20", "20 min", 20),
            Preset("p25", "Focus", 25),
            Preset("p45", "45 min", 45),
            Preset("p60", "1 hr", 60)
        )
    }
    var showCustom by remember { mutableStateOf(false) }
    Surface(
        color = Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DarkScreen",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            CurrentTime()
            Spacer(Modifier.height(28.dp))

            PresetRow(
                presets = presets,
                onSelect = { onStart(it.minutes) },
                onCustom = { showCustom = true }
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onStart(25) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentDim,
                    contentColor = TextPrimary
                )
            ) { Text("Start Focus") }
        }
    }

    if (showCustom) {
        CustomPresetDialog(
            onDismiss = { showCustom = false },
            onConfirm = { minutes ->
                showCustom = false
                onStart(minutes)
            }
        )
    }
}

@Composable
private fun CurrentTime() {
    val time by com.example.darkscreen.util.clockFlow().collectAsState(inital = "--:--")
    Text(
        text = time,
        color = TextPrimary,
        style = MaterialTheme.run {
            typography.displayLarge.copy(
                fontweight = FontWeight.Light
            )
        }
    )
}

@Composable
private fun PresetRow(
    presets: List<Preset>,
    onSelect: (Preset) -> Unit,
    onCustom: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(presets) { p ->
            PresetCard(label = p.label) { onSelect(p) }
        }
        item {
            PresetCard(label = "Custom", accent = true) { onCustom() }
        }
    }
}

@Composable
private fun PresetCard(label: String, accent: Boolean = false, onClick: () -> Unit) {
    Surface(
        color = if (accent) AccentDim else DeepGrey,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .height(56.dp)
            .widthIn(min = 96.dp)
            .clickable { onClick() }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(label, color = TextPrimary)
        }
    }
}

@Composable
fun CustomPresetDialog(onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var minutesText by remember { mutableStateOf("25") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val m = minutesText.toIntOrNull()
                if (m != null && m in 1..180) onConfirm(m)
            }) { Text("Start") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = {Text("Custom Preset") },
        text = {
            OutlinedTextField(
                value = minutesText,
                onValueChange = { minutesText = it.filter { ch -> ch.isDigit() }.take(3) },
                singleLine = true,
                label = { Text("Minutes (1-180)") }
            )
        }
    )
}
