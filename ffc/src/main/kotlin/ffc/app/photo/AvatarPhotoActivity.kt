package ffc.app.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import com.sembozdemir.permissionskt.askPermissions
import com.sembozdemir.permissionskt.handlePermissionsResult
import ffc.android.load
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.app.FamilyFolderActivity
import ffc.app.R
import kotlinx.android.synthetic.main.activity_photo_avatar.avatarView
import kotlinx.android.synthetic.main.activity_photo_avatar.choosePhoto
import kotlinx.android.synthetic.main.activity_photo_avatar.takePhoto
import me.piruin.phototaker.PhotoSize
import me.piruin.phototaker.PhotoTaker
import me.piruin.phototaker.PhotoTakerListener
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class AvatarPhotoActivity : FamilyFolderActivity() {

    private val photoTaker by lazy {
        PhotoTaker(this, PhotoSize(1024, 1024)).apply {
            setListener(object : PhotoTakerListener {
                override fun onFinish(intent: Intent?) {
                    photoUri = TakePhotoActivity.UriPhoto(intent!!.data!!)
                    avatarView.load(intent.data!!)
                }

                override fun onCancel(action: Int) {}

                override fun onError(action: Int) {}
            })
        }
    }

    private var previousPhotoUrl: TakePhotoActivity.UrlPhoto? = null
    var photoUri: TakePhotoActivity.UriPhoto? = null

    private val storage: PhotoStorage
        get() = org!!.photoStorageFor(intent.photoType)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_avatar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.data?.let {
            previousPhotoUrl = TakePhotoActivity.UrlPhoto(it.toString())
            avatarView.load(it)
        }

        takePhoto.onClick {
            askPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                onGranted {
                    photoTaker.captureImage()
                }
            }

        }
        choosePhoto.onClick {
            askPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                onGranted {
                    toast(R.string.under_construction)
                    //photoTaker.pickImage()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        handlePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoTaker.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (photoUri == null) finish()
                photoUri?.let { photo -> upload(photo) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun upload(photo: TakePhotoActivity.Photo) {
        val dialog = indeterminateProgressDialog("บันทึกภาพถ่าย").apply { show() }
        storage.save(photo.uri) {
            onComplete {
                delete(previousPhotoUrl)
                dialog.dismiss()
                setResult(Activity.RESULT_OK, Intent().apply { data = it })
                finish()
            }
        }
    }

    private fun delete(photo: TakePhotoActivity.Photo?) {
        photo?.let {
            storage.delete(it.uri) {}
        }
    }
}

fun Activity.startAvatarPhotoActivity(
    type: PhotoType,
    url: String? = null,
    avatarView: View? = null,
    requestCode: Int = REQUEST_TAKE_PHOTO
) {
    val intent = intentFor<AvatarPhotoActivity>()
    intent.photoType = type
    url?.let { intent.data = Uri.parse(it) }
    val bundle = if (avatarView == null)
        sceneTransition()
    else
        sceneTransition(avatarView to getString(R.string.transition_photo))
    startActivityForResult(intent, requestCode, bundle)
}
