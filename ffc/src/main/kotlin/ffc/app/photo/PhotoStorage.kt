package ffc.app.photo

import android.net.Uri
import ffc.app.util.TaskCallback
import ffc.entity.Organization

interface PhotoStorage {

    fun save(uri: Uri, callback: TaskCallback<Uri>.() -> Unit)

    fun delete(uri: Uri, callback: TaskCallback<Boolean>.() -> Unit)
}

fun Organization.photoStorageFor(photoType: PhotoType): PhotoStorage = FirebasePhotoStorage(this, photoType)
