package ffc.app.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
//import android.support.v4.content.FileProvider
import org.jetbrains.anko.intentFor
import java.io.File
import java.io.IOException
import java.util.UUID

private fun takePhotoIntent(uri: Uri): Intent {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    intent.putExtra("return-data", true)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    return intent
}

internal fun Activity.takePhoto(requestCode: Int = REQUEST_TAKE_PHOTO): Uri {
    val uri = FileProvider.getUriForFile(this, packageName + ".provider", createImageFile())
    startActivityForResult(takePhotoIntent(uri), requestCode)
    return uri
}

internal fun Activity.choosePhoto(requestCode: Int = REQUEST_PICK_PHOTO) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"
    startActivityForResult(intent, requestCode)
}

fun Context.viewPhoto(uri: Uri, bundle: Bundle? = null) {
    val intent = intentFor<PhotoActivity>()
    intent.data = uri
    startActivity(intent, bundle)
}

fun Context.viewPhoto(url: String, bundle: Bundle? = null) {
    viewPhoto(Uri.parse(url), bundle)
}

@Throws(IOException::class)
internal fun Context.createImageFile(): File {
    val imageFileName = UUID.randomUUID().toString().replace("-", "")
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}
