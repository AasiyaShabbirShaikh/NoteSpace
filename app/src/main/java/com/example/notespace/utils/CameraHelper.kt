package com.example.notespace

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object CameraHelper {

    private var imageUri: Uri? = null

    fun handleCameraAction(
        context: Context,
        contentResolver: ContentResolver,
        cameraResultLauncher: ActivityResultLauncher<Intent>,
        permissionLauncher: ActivityResultLauncher<String>
    ) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera(context, contentResolver, cameraResultLauncher)
        } else {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun openCamera(
        context: Context,
        contentResolver: ContentResolver,
        cameraResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "ImageTitle")
            put(MediaStore.Images.Media.DESCRIPTION, "ImageDescription")
        }

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (imageUri != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            cameraResultLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Failed to create image URI", Toast.LENGTH_SHORT).show()
        }
    }

    fun getImageUri(): Uri? {
        return imageUri
    }
}
