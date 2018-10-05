package ffc.app.photo

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import ffc.app.util.TaskCallback
import ffc.entity.Organization

class FirebasePhotoStorage(val org: Organization, val photoType: PhotoType) : PhotoStorage {

    val storage by lazy { FirebaseStorage.getInstance("gs://${org.name}-bucket") }
    val folder by lazy { storage.getReference("images").child(photoType.folder) }

    override fun save(uri: Uri, callback: TaskCallback<Uri>.() -> Unit) {
        val taskCallback = TaskCallback<Uri>().apply(callback)
        val ref = folder.child(uri.lastPathSegment!!)
        ref.putFile(uri).addOnFailureListener {
            taskCallback.expception?.invoke(it)
        }.continueWith(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let { taskCallback.expception?.invoke(it) }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                taskCallback.result.invoke(task.result!!.result!!)
            } else {
                task.exception?.let { taskCallback.expception?.invoke(it) }
            }
        }
    }

    override fun delete(uri: Uri, callback: TaskCallback<Boolean>.() -> Unit) {
        //TODO Implement
    }
}
