package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.R
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.CashanovaBackground
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.

@Composable
fun LoginScreen(navController: NavController, viewModel: CashanovaViewModel) {

    var username        by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginError      by viewModel.loginError.collectAsState()

    CashanovaBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Cashanova logo",
                modifier = Modifier
                    .width(190.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── App name ──────────────────────────────────────────────────────
            Text(
                text = "CASHANOVA",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Gold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "WELCOME USER",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Username field ────────────────────────────────────────────────
            GoldTextField(
                value = username,
                onValueChange = {
                    username = it
                    viewModel.clearLoginError()
                },
                label = "USERNAME:"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Forgot your username?",
                color = TextGray,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Password field ────────────────────────────────────────────────
            GoldTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearLoginError()
                },
                label = "PASSWORD:",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle password",
                        tint = Gold,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Forgot your password?",
                color = TextGray,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            // ── Error message ─────────────────────────────────────────────────
            if (loginError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = loginError!!,
                    color = ErrorRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Login button ──────────────────────────────────────────────────
            GoldButton(
                text = "LOGIN",
                onClick = {
                    viewModel.login(username, password) {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Register link ─────────────────────────────────────────────────
            Row {
                Text(
                    text = "No Account? ",
                    color = TextGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "REGISTER NOW",
                    color = Gold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
