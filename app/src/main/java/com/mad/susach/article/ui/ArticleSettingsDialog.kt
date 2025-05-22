package com.mad.susach.article.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArticleSettingsDialog(
    show: Boolean,
    currentBgColor: Color,
    currentFontSize: Float,
    onBgColorChange: (Color) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    val bgColors = listOf(Color.White, Color(0xFFFDF6F0), Color(0xFFFFF8E1), Color(0xFFE3F2FD))
    val fontSizes = listOf(14f, 16f, 18f, 20f, 22f)

    var selectedBgColor by remember { mutableStateOf(currentBgColor) }
    var selectedFontSize by remember { mutableStateOf(currentFontSize) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tuỳ chỉnh hiển thị") },
        text = {
            Column {
                Text("Màu nền", style = MaterialTheme.typography.bodyMedium)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    bgColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color)
                                .border(
                                    width = if (color == selectedBgColor) 3.dp else 1.dp,
                                    color = if (color == selectedBgColor) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable { selectedBgColor = color }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Cỡ chữ", style = MaterialTheme.typography.bodyMedium)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    fontSizes.forEach { size ->
                        Button(
                            onClick = { selectedFontSize = size },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (size == selectedFontSize) MaterialTheme.colorScheme.primary else Color.LightGray
                            ),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text(size.toInt().toString(), fontSize = size.sp, color = Color.Black)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onBgColorChange(selectedBgColor)
                onFontSizeChange(selectedFontSize)
                onDismiss()
            }) {
                Text("Áp dụng")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}

