package com.cashanova.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cashanova.app.R
import com.cashanova.app.ui.theme.Black
import com.cashanova.app.ui.theme.Gold
import com.cashanova.app.ui.theme.GoldDark

@Composable
fun CashanovaBackground(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Black)) {
        Image(
            painter = painterResource(id = R.drawable.app_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.52f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Black.copy(alpha = 0.18f),
                            Black.copy(alpha = 0.42f),
                            Black.copy(alpha = 0.20f)
                        )
                    )
                ),
            content = content
        )
    }
}

fun Modifier.cashaNovaPanel(): Modifier = this
    .clip(RoundedCornerShape(18.dp))
    .background(Black.copy(alpha = 0.78f))
    .border(1.dp, GoldDark.copy(alpha = 0.75f), RoundedCornerShape(18.dp))
    .padding(14.dp)

fun Modifier.cashaNovaField(): Modifier = this
    .clip(RoundedCornerShape(50.dp))
    .background(Black.copy(alpha = 0.72f))
    .border(1.dp, Gold.copy(alpha = 0.55f), RoundedCornerShape(50.dp))
