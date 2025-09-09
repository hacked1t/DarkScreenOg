package com.example.darkscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PresetSelector(
    selectedPreset: PresetData?,
    onPresetSelected: (PresetData?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Dark Presets",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        //Preset Cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            //Custom mode card
            item {
                PresetCard(
                    preset = null,
                    isSelected = selectedPreset == null,
                    onClick = { onPresetSelected(null) }
                )
            }
            items(DarkPresets.allPresets) { preset ->
                PresetCard(
                    preset = preset,
                    isSelected = selectedPreset?.name == preset.name,
                    onClick = { onPresetSelected(preset) }
                )
            }
        }
        selectedPreset?.let { preset ->
            Text(
                text = preset.description,
                fontSize = 14.sp,
                color = preset.accentColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
        } ?: run {
            Text(
                text = "Manual darkness control",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
private fun PresetCard(
    preset: PresetData?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = preset?.overlayColor?.copy(alpha = 0.3f)
        ?: Color.White.copy(alpha =  0.11f)
    val borderColor = if (isSelected) {
        preset?.accentColor ?: Color.White
    } else {
        Color.Transparent
    }

    Card(
        modifier = modifier
            .size(width = 120.dp, height = 100.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        (preset?.accentColor ?: Color.White).copy(
                            alpha = 0.2f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = preset?.emoji ?: "⚙\uFE0F",
                    fontSize = 16.sp
                )
            }
            Text(
                text = preset?.name ?: "Custom",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = preset?.accentColor ?: Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


