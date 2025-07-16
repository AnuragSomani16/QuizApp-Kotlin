package com.example.quizapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect == true -> Color(0xFF4CAF50)
        isCorrect == false && isSelected -> Color(0xFFF44336)
        isSelected -> Color(0xFFBBDEFB)
        else -> Color.White
    }

    val icon = when {
        isCorrect == true -> Icons.Default.Check
        isCorrect == false && isSelected -> Icons.Default.Close
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = isCorrect == null, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}