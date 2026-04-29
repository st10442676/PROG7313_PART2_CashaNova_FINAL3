package com.cashanova.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cashanova.app.ui.components.GoldButton
import com.cashanova.app.ui.components.GoldTextField
import com.cashanova.app.ui.theme.*
import com.cashanova.app.viewmodel.CashanovaViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.O)
@Composable
fun CreateExpenseScreen(navController: NavController, viewModel: CashanovaViewModel) {
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(-1) }
    var date by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoPath by remember { mutableStateOf<String?>(null) }
    var tempImageFile by remember { mutableStateOf<File?>(null) }

    val categories by viewModel.getCategories().collectAsState(initial = emptyList())

    // ── Create temp file for camera ───────────────────────────────────────────
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Pictures")
        return File.createTempFile("EXPENSE_${timeStamp}_", ".jpg", storageDir)
    }

    // ── Camera launcher ───────────────────────────────────────────────────────
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = tempImageFile?.let {
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
            }
            photoPath = tempImageFile?.absolutePath
        }
    }

    // ── Gallery launcher ──────────────────────────────────────────────────────
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            photoUri = it
            photoPath = it.toString()
        }
    }

    // ── Camera permission launcher ────────────────────────────────────────────
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = createImageFile()
            tempImageFile = file
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            cameraLauncher.launch(uri)
        }
    }

    val iconMap = mapOf<String, ImageVector>(
        "ShoppingCart" to Icons.Filled.ShoppingCart,
        "Restaurant" to Icons.Filled.Restaurant,
        "DirectionsCar" to Icons.Filled.DirectionsCar,
        "Home" to Icons.Filled.Home,
        "LocalHospital" to Icons.Filled.LocalHospital,
        "School" to Icons.Filled.School,
        "Movie" to Icons.Filled.Movie,
        "Checkroom" to Icons.Filled.Checkroom,
        "Pets" to Icons.Filled.Pets,
        "SportsSoccer" to Icons.Filled.SportsSoccer,
        "Flight" to Icons.Filled.Flight,
        "Phone" to Icons.Filled.Phone,
        "Wifi" to Icons.Filled.Wifi,
        "LocalGasStation" to Icons.Filled.LocalGasStation,
        "Coffee" to Icons.Filled.Coffee,
        "Savings" to Icons.Filled.Savings
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // ── Header ────────────────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Gold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "ADD EXPENSE",
                color = Gold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Amount ────────────────────────────────────────────────────────────
        GoldTextField(
            value = amount,
            onValueChange = { amount = it; saved = false; errorMsg = "" },
            label = "Amount (R)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Description ───────────────────────────────────────────────────────
        GoldTextField(
            value = description,
            onValueChange = { description = it; saved = false; errorMsg = "" },
            label = "Description"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Date ──────────────────────────────────────────────────────────────
        GoldTextField(
            value = date,
            onValueChange = { date = it },
            label = "Date (yyyy-MM-dd)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Start & End Time ──────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GoldTextField(
                value = startTime,
                onValueChange = { startTime = it },
                label = "Start Time (optional)",
                modifier = Modifier.weight(1f)
            )
            GoldTextField(
                value = endTime,
                onValueChange = { endTime = it },
                label = "End Time (optional)",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Photo Section ─────────────────────────────────────────────────────
        Text("RECEIPT PHOTO", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (photoUri != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Receipt photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { photoUri = null; photoPath = null },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ErrorRed)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Remove",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Camera button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackCard)
                        .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable {
                            val permissionCheck = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.CAMERA
                            )
                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                val file = createImageFile()
                                tempImageFile = file
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                cameraLauncher.launch(uri)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.CameraAlt,
                            contentDescription = "Camera",
                            tint = Gold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Take Photo", color = TextWhite, fontSize = 12.sp)
                    }
                }

                // Gallery button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackCard)
                        .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Photo,
                            contentDescription = "Gallery",
                            tint = Gold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Choose Photo", color = TextWhite, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Category Selection ────────────────────────────────────────────────
        Text("SELECT CATEGORY", color = TextGray, fontSize = 12.sp, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (categories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BlackCard)
                    .padding(16.dp)
            ) {
                Text(
                    "No categories yet. Create one first.",
                    color = TextGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            categories.forEach { cat ->
                val catColor = try {
                    Color(android.graphics.Color.parseColor(cat.colorHex))
                } catch (e: Exception) { Gold }
                val catIcon = iconMap[cat.iconName] ?: Icons.Filled.Star
                val isSelected = selectedCategoryId == cat.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) catColor.copy(alpha = 0.15f) else BlackCard)
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) catColor else Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedCategoryId = cat.id; saved = false; errorMsg = "" }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(catColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            catIcon,
                            contentDescription = null,
                            tint = catColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        cat.name,
                        color = if (isSelected) catColor else TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = catColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // ── Error & Success messages ───────────────────────────────────────────
        if (errorMsg.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                errorMsg,
                color = ErrorRed,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (saved) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "✓ Expense saved!",
                color = SuccessGreen,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Save Button ───────────────────────────────────────────────────────
        GoldButton(
            text = "SAVE EXPENSE",
            onClick = {
                when {
                    amount.isBlank() -> errorMsg = "Please enter an amount"
                    description.isBlank() -> errorMsg = "Please enter a description"
                    selectedCategoryId == -1 -> errorMsg = "Please select a category"
                    else -> {
                        viewModel.createExpense(
                            categoryId  = selectedCategoryId,
                            amount      = amount.toDoubleOrNull() ?: 0.0,
                            description = description,
                            date        = date,
                            startTime   = startTime,
                            endTime     = endTime,
                            photoPath   = photoPath
                        )
                        amount             = ""
                        description        = ""
                        selectedCategoryId = -1
                        startTime          = ""
                        endTime            = ""
                        photoUri           = null
                        photoPath          = null
                        saved              = true
                        errorMsg           = ""
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}