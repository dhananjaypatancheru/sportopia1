package com.dhanunjay.sportopia

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhanunjay.sportopia.ui.Theme.SportopiaTheme

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

class UserProfileActivity : ComponentActivity() {

    enum class CameraPermissionStatus { NoPermission, PermissionGranted, PermissionDenied }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraPermissionStatusState = mutableStateOf(CameraPermissionStatus.NoPermission)
        val photoUriState: MutableState<Uri?> = mutableStateOf(null)
        val hasPhotoState = mutableStateOf(value = false)
        val resolver = applicationContext.contentResolver

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted: Boolean ->
                if (isGranted) {
                    cameraPermissionStatusState.value = CameraPermissionStatus.PermissionGranted
                } else {
                    cameraPermissionStatusState.value = CameraPermissionStatus.PermissionDenied
                }
            }

        val takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
                hasPhotoState.value = isSaved
            }

        val takePhoto: () -> Unit = {
            hasPhotoState.value = false

            val values = ContentValues().apply {
                val title = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                put(MediaStore.Images.Media.TITLE, "Compose Camera Example Image - $title")
                put(MediaStore.Images.Media.DISPLAY_NAME, title)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }

            val uri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            takePhotoLauncher.launch(uri)
            photoUriState.value = uri

        }

        // Ideally these would be cached instead of reloaded
        val getThumbnail: (Uri?) -> ImageBitmap? = { uri ->
            val targetSize = 256f
            println("URI is $uri")
            uri?.let {
                println("Opening Input Stream")
                resolver.openInputStream(it)
            }?.let {
                BitmapFactory.decodeStream(it)
            }?.let {
                val height = it.height.toFloat()
                val width = it.width.toFloat()
                val scaleFactor = min(targetSize / height, targetSize / width)
                Bitmap.createScaledBitmap(it, (scaleFactor * width).toInt() , (scaleFactor * height).toInt(), true)
            }?.let {
                val rotation = getImageRotation(resolver, uri)
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, Matrix().apply { postRotate(rotation.toFloat()) }, true)
            }?.asImageBitmap()

        }

        val getFullImage: (Uri?) -> ImageBitmap? = { uri ->
            uri?.let {
                resolver.openInputStream(it)
            }?.let {
                BitmapFactory.decodeStream(it)
            }?.let {
                val rotation = getImageRotation(resolver, uri)
                Bitmap.createBitmap(it, 0, 0, it.width, it.height, Matrix().apply { postRotate(rotation.toFloat()) }, true)
            }?.asImageBitmap()

        }

        setContent {
            val cameraPermissionStatus by remember { cameraPermissionStatusState }
            val hasPhoto by remember { hasPhotoState }
            var shouldShowFullImage by remember { mutableStateOf(false) }
            var isEditing by remember { mutableStateOf(false) }
            var name by remember { mutableStateOf("Dhanunjay") }
            var email by remember { mutableStateOf("d@gmail.com") }
            var location by remember { mutableStateOf("uk") }

            SportopiaTheme() {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TakePhotoButton(
                            cameraPermissionStatus = cameraPermissionStatus,
                            requestPermissionLauncher = requestPermissionLauncher,
                            takePhoto = takePhoto
                        )
                        if (hasPhoto) {
                            val bitmap = getThumbnail(photoUriState.value)
                            println("Is bitmap null: $bitmap")
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = "Thumbnail of Save Photo",
                                    modifier = Modifier.clickable {
                                        shouldShowFullImage = true
                                    }
                                )
                            }
                        }

                        if (isEditing) {
                            TextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Id") },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            TextField(
                                value = location,
                                onValueChange = { location = it },
                                label = { Text("Location") },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            Button(
                                onClick = { isEditing = false },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text("Save", color = Color.White)
                            }
                        } else {
                            HeadingTextComponent(value = "Name: $name")
                            Spacer(modifier = Modifier.height(15.dp))

                            HeadingTextComponent(value = "Email Id: $email")
                            Spacer(modifier = Modifier.height(15.dp))

                            HeadingTextComponent(value = "Location: $location")
                            Spacer(modifier = Modifier.height(15.dp))

                            Button(
                                onClick = { isEditing = true },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text("Edit", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))
                        Button(
                            onClick = { finish() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text("Back", color = Color.White)
                        }
                    }

                    if (shouldShowFullImage && hasPhoto) {
                        val bitmap = getFullImage(photoUriState.value)
                        if (bitmap != null) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    shouldShowFullImage = false
                                }) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = "Full image of Save Photo",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                                androidx.compose.material.Surface(
                                    modifier = Modifier
                                        .background(androidx.compose.material.MaterialTheme.colors.background)
                                        .align(Alignment.Center)
                                        .padding(8.dp)
                                ) {
                                    androidx.compose.material.Text(
                                        text = "Click to Close",
                                        style = androidx.compose.material.MaterialTheme.typography.h4.copy(
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                }
                            }

                        } else {
                            shouldShowFullImage = false
                        }

                    }
                }
            }
        }
    }

    private fun getImageRotation(resolver: ContentResolver, uri: Uri): Int {
        val cursor = resolver.query(uri, arrayOf(MediaStore.Images.Media.ORIENTATION), null,
            null, null)
        var result = 0

        cursor?.apply {
            moveToFirst()
            val index = getColumnIndex(MediaStore.Images.Media.ORIENTATION)
            result = getInt(index)
            close()
        }
        println("Rotation = $result")
        return result
    }
}

@Composable
fun TakePhotoButton(
    cameraPermissionStatus: UserProfileActivity.CameraPermissionStatus,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    takePhoto: () -> Unit
) {
    OutlinedButton(
        onClick = {
            when (cameraPermissionStatus) {
                UserProfileActivity.CameraPermissionStatus.NoPermission ->
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)

                UserProfileActivity.CameraPermissionStatus.PermissionGranted ->
                    takePhoto()

                UserProfileActivity.CameraPermissionStatus.PermissionDenied -> {}

            }
        },
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Red)
    ) {
        when (cameraPermissionStatus) {
            UserProfileActivity.CameraPermissionStatus.NoPermission ->
                Text(text = " Camera", color = Color.White)

            UserProfileActivity.CameraPermissionStatus.PermissionDenied ->
                Text(text = "Camera Permissions Have Been Denied", color = Color.White)

            UserProfileActivity.CameraPermissionStatus.PermissionGranted ->
                Text(text = "Take Photo", color = Color.White)
        }
    }
}
