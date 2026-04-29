package com.cashanova.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cashanova.app.navigation.Routes
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel

//Phillips, B., Stewart, C. and Marsicano, K. 2019. Android Programming: The Big Nerd Ranch Guide. 4th ed. Atlanta: Big Nerd Ranch.

@Composable
fun RegisterScreen(navController: NavController, viewModel: CashanovaViewModel) {

    var fullName        by remember { mutableStateOf("") }
    var surname         by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var contactNumber   by remember { mutableStateOf("") }
    var day             by remember { mutableStateOf("") }
    var month           by remember { mutableStateOf("") }
    var year            by remember { mutableStateOf("") }
    var username        by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passVisible     by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }
    val registerError   by viewModel.registerError.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Black, Color(0xFF0D0D0D))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // ── Title ─────────────────────────────────────────────────────────
            Text(
                text = "REGISTRATION",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Gold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Full Name & Surname row ────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GoldTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        viewModel.clearRegisterError()
                    },
                    label = "Full Name",
                    modifier = Modifier.weight(1f)
                )
                GoldTextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        viewModel.clearRegisterError()
                    },
                    label = "Surname",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Email ─────────────────────────────────────────────────────────
            GoldTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearRegisterError()
                },
                label = "Email Address",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Contact Number ────────────────────────────────────────────────
            GoldTextField(
                value = contactNumber,
                onValueChange = {
                    contactNumber = it
                    viewModel.clearRegisterError()
                },
                label = "Contact Number",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Date of Birth ─────────────────────────────────────────────────
            Text(
                text = "Date of Birth",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // DD
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .background(DarkGray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = day,
                        onValueChange = { if (it.length <= 2) day = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Gold),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { inner ->
                            if (day.isEmpty()) Text(
                                "DD",
                                color = TextGray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            ) else inner()
                        }
                    )
                }
                // Divider
                Text("|", color = TextGray, fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterVertically))
                // MM
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .background(DarkGray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = month,
                        onValueChange = { if (it.length <= 2) month = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Gold),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { inner ->
                            if (month.isEmpty()) Text(
                                "MM",
                                color = TextGray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            ) else inner()
                        }
                    )
                }
                // Divider
                Text("|", color = TextGray, fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterVertically))
                // YYYY
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                        .background(DarkGray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = year,
                        onValueChange = { if (it.length <= 4) year = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Gold),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { inner ->
                            if (year.isEmpty()) Text(
                                "YYYY",
                                color = TextGray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            ) else inner()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Username ──────────────────────────────────────────────────────
            GoldTextField(
                value = username,
                onValueChange = {
                    username = it
                    viewModel.clearRegisterError()
                },
                label = "Username"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Password ──────────────────────────────────────────────────────
            GoldTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearRegisterError()
                },
                label = "Create Password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (passVisible)
                            Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle",
                        tint = Gold,
                        modifier = Modifier.clickable { passVisible = !passVisible }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Confirm Password ──────────────────────────────────────────────
            GoldTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    viewModel.clearRegisterError()
                },
                label = "Confirm Password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmVisible)
                            Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle",
                        tint = Gold,
                        modifier = Modifier.clickable { confirmVisible = !confirmVisible }
                    )
                }
            )

            // ── Error ─────────────────────────────────────────────────────────
            if (registerError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = registerError!!,
                    color = ErrorRed,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Sign Up button ────────────────────────────────────────────────
            GoldButton(
                text = "SIGN UP →",
                onClick = {
                    val dob = "$day/$month/$year"
                    viewModel.register(
                        fullName        = fullName,
                        surname         = surname,
                        email           = email,
                        contactNumber   = contactNumber,
                        dateOfBirth     = dob,
                        username        = username,
                        password        = password,
                        confirmPassword = confirmPassword
                    ) {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ── Back to login ─────────────────────────────────────────────────
            Row {
                Text("Already have an account? ", color = TextGray, fontSize = 14.sp)
                Text(
                    text = "LOGIN",
                    color = Gold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}