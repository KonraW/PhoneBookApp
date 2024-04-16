//package com.example.phonebookapp.ui.home
//
//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.FragmentActivity
//
//// Funkcja pomocnicza do sprawdzenia uprawnień
//fun hasReadStoragePermission(activity: FragmentActivity): Boolean {
//    return ContextCompat.checkSelfPermission(
//        activity,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    ) == PackageManager.PERMISSION_GRANTED
//}
//
//// Komponent Compose do poproszenia użytkownika o uprawnienia
//@Composable
//fun RequestStoragePermission(
//    activity: FragmentActivity,
//    onPermissionGranted: () -> Unit,
//    onPermissionDenied: () -> Unit
//) {
//    val requestPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // Uprawnienie zostało udzielone
//            onPermissionGranted()
//        } else {
//            // Uprawnienie zostało odrzucone
//            onPermissionDenied()
//        }
//    }
//
//    val requestPermission = remember {
//        {
//            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//    }
//
//    // Sprawdź, czy uprawnienie jest już udzielone
//    if (hasReadStoragePermission(activity)) {
//        // Uprawnienie jest już udzielone
//        onPermissionGranted()
//    } else {
//        // Poproś użytkownika o uprawnienie
//        requestPermission()
//    }
//}
