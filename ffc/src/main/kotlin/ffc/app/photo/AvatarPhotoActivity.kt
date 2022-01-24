package ffc.app.photo

//import ffc.app.person
//import ffc.app.person.personId
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sembozdemir.permissionskt.askPermissions
import com.sembozdemir.permissionskt.handlePermissionsResult
import ffc.android.*
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.dev
import ffc.app.person.PersonActivitiy
import ffc.app.util.alert.handle
import ffc.app.util.alert.toast
import kotlinx.android.synthetic.main.activity_person_popup.*
import kotlinx.android.synthetic.main.photo_action_bar.*
import kotlinx.android.synthetic.main.photo_avatar_activity.avatarView
import me.piruin.phototaker.PhotoSize
import me.piruin.phototaker.PhotoTaker
import me.piruin.phototaker.PhotoTakerListener
import org.jetbrains.anko.dip
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AvatarPhotoActivity : FamilyFolderActivity() {
    private var CAMERA_REQUEST_CODE=2034;
    private var mStorage: StorageReference? = null;

    lateinit var personViewModel: PersonActivitiy.PersonViewModel
    private val photoTaker by lazy {
        PhotoTaker(this, PhotoSize(dip(1024), dip(1024))).apply {
            setListener(object : PhotoTakerListener {
                override fun onFinish(intent: Intent) {
                    photoUri = UriPhoto(intent.data!!)
                    avatarView.load(intent.data!!)
                }

                override fun onCancel(action: Int) {}

                override fun onError(action: Int) {}
            })
        }
    }

    private var previousPhotoUrl: UrlPhoto? = null
    var photoUri: Photo? = null

    private val storage: PhotoStorage
        get() = org!!.photoStorageFor(intent.photoType)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_avatar_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mStorage = FirebaseStorage.getInstance().reference;
        intent.data?.let {
            previousPhotoUrl = UrlPhoto(it.toString())
            avatarView.load(it)
        }
        personViewModel = viewModel()
        takePhoto.onClick {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
            askPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                onGranted {
                    photoTaker.captureImage()
//                    dispatchTakePictureIntent();
                }
            }
        }
        }
        choosePhoto.onClick {
            askPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                onGranted {
                    photoTaker.pickImage()
                }
            }
        }

    }
    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("TAG", "Permission is granted")
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted")
            true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        photoTaker.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1030) {
            if (data != null) {
                val extras = data.extras
                val imageBitmap = extras?.get("data") as Bitmap
                avatarView.setImageBitmap(imageBitmap)
//                val baos = ByteArrayOutputStream()
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                photoUri = UriPhoto(getImageUri(applicationContext, imageBitmap)!!)

//                val finalFile = File(getRealPathFromURI(photoUri))

            }
        }
        else {
            photoTaker.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
    fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }
    private fun genFileName(): String? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return "JPG_" + timeStamp + "_" + UUID.randomUUID().toString() + ".jpg"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (photoUri == null) finish()
                photoUri?.let { photo -> upload(photo) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }
    private fun upload(photo: Photo) {
        val dialog = indeterminateProgressDialog("บันทึกภาพถ่าย").apply { show() }
        storage.save(photo.uri) {
            onComplete {
                delete(previousPhotoUrl)
                dialog.dismiss()
                setResult(Activity.RESULT_OK, Intent().apply { data = it })
                finish()
            }
            onFail {
                handle(it)
            }
        }
    }

    private fun delete(photo: Photo?) {
        photo?.let { photo ->
            storage.delete(photo.uri) {
                onComplete { dev { toast("Deleted ${photo.uri}") } }
                onFail { handle(it) }
            }
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
