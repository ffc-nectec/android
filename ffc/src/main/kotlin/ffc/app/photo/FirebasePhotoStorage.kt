package ffc.app.photo

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import ffc.app.util.TaskCallback
import ffc.entity.Organization
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

internal class FirebasePhotoStorage(val org: Organization, val photoType: PhotoType) : PhotoStorage {

    val storage by lazy { FirebaseStorage.getInstance() }
    val folder by lazy {
        storage.getReference("images")
            .child(org.id)
            .child(photoType.folder)
    }

    override fun save(uri: Uri, callback: TaskCallback<Uri>.() -> Unit) {
        val taskCallback = TaskCallback<Uri>().apply(callback)
        val ref = folder.child("${timestamp()}-${uri.lastPathSegment}")
        ref.putFile(uri).addOnFailureListener {
            taskCallback.expception!!.invoke(it)
        }.addOnSuccessListener { _ ->
            ref.downloadUrl.addOnSuccessListener {
                taskCallback.result(it)
            }.addOnFailureListener {
                taskCallback.expception!!.invoke(it)
            }
        }
    }

    override fun delete(uri: Uri, callback: TaskCallback<Boolean>.() -> Unit) {
        val taskCallback = TaskCallback<Boolean>().apply(callback)
        //lastPathSegment of download url is full reference path
        val photoRef = storage.getReference(uri.lastPathSegment.toString())
        photoRef.delete().addOnSuccessListener {
            taskCallback.result(true)
        }.addOnFailureListener {
            taskCallback.expception!!.invoke(it)
        }
    }
}

fun timestamp(): String = DateTimeFormat.forPattern("yyyyMMddHHmmss").print(DateTime.now())
