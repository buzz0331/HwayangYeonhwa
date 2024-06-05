package com.example.teamproject.Mainactivity.location

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlaceCell(location_name: String, onClick:()->Unit) {
    Box(
        modifier = Modifier.height(50.dp).fillMaxWidth()
            .border(width = 4.dp, color = Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = location_name,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}