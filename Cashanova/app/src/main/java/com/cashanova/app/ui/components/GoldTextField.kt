package com.cashanova.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashanova.app.ui.theme.*

@Composable
fun GoldTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = DarkGray,
                    shape = RoundedCornerShape(26.dp)
                )
                .border(
                    width = 1.dp,
                    color = Gold.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = TextStyle(
                    color = TextWhite,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(Gold),
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth(if (trailingIcon != null) 0.85f else 1f)
            )
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    trailingIcon()
                }
            }
        }
    }
}