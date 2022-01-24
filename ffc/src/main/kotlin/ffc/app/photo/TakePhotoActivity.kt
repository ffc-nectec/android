package ffc.app.photo

//import android.support.v4.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import ffc.android.SpacesItemDecoration
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.setting.AboutActivity
import ffc.app.util.alert.handle
import kotlinx.android.synthetic.main.photo_action_bar.*
import kotlinx.android.synthetic.main.photo_take_activity.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor


//import org.jetbrains.anko.support.v4.intentFor

val REQUEST_TAKE_PHOTO = 2034
val REQUEST_PICK_PHOTO = 2035

class TakePhotoActivity : FamilyFolderActivity() {

    private var photoList = mutableListOf<Photo>()
    private var targetImageUri: Uri? = null

    private val storage: PhotoStorage
        get() = org!!.photoStorageFor(intent.photoType)

    private val RecyclerView.takePhotoAdapter: TakePhotoAdapter
        get() = adapter as TakePhotoAdapter

    private val maxPhotoSize get() = intent.limit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_take_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.urls?.forEach {
            photoList.add(UrlPhoto(it))
        }
        with(photos) {
            addItemDecoration(SpacesItemDecoration(dip(16)))
            layoutManager = LinearLayoutManager(context,  VERTICAL, true)
            adapter = TakePhotoAdapter(photoList) {
                onViewClick { view, photo ->
                    when (view.id) {
                        R.id.image -> viewPhoto(photo.uri,
                            sceneTransition(view to getString(R.string.transition_photo)))
                        R.id.delBtn -> removeImage(photo)
                    }
                }
            }
        }
        takePhoto.onClick { targetImageUri = takePhoto(REQUEST_TAKE_PHOTO) }
        choosePhoto.onClick { choosePhoto(REQUEST_PICK_PHOTO) }

        if (photoList.isEmpty()) {
            takePhoto.performClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photo_take, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.done,
            android.R.id.home -> {
                val dialog = indeterminateProgressDialog("บันทึกภาพถ่าย") {
                    setCancelable(false)
                    show()
                }
                photoList.upload {
                    dialog.dismiss()
                    val intent = Intent().apply {
                        urls = it
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun MutableList<Photo>.upload(function: (List<String>) -> Unit) {
        val needUpload = this.sumBy { if (it is UriPhoto) 1 else 0 }
        if (needUpload > 0) {
            val urls = mutableListOf<String>()
            forEach { photo ->
                when (photo) {
                    is UriPhoto -> storage.save(photo.uri) {
                        onComplete {
                            urls.add(it.toString())
                            if (urls.size == this@upload.size) {
                                function(urls)
                            }
                        }
                        onFail {
                            handle(it)
                            function(listOf())
                        }
                    }
                    is UrlPhoto -> {
                        urls.add(photo.url)
                        if (urls.size == this.size) {
                            function(urls)
                        }
                    }
                }
            }
        } else {
            function(map { it.uri.toString() })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> addPhoto(targetImageUri!!)
                REQUEST_PICK_PHOTO -> result?.data?.let { addPhoto(it) }
            }
        }
    }

    private fun addPhoto(uri: Uri) {
        if (photoList.size == maxPhotoSize)
            photoList.removeAt(0)
        photoList.add(UriPhoto(uri))
        photos.takePhotoAdapter.update(photoList)
    }

    private fun removeImage(photo: Photo) {
        when (photo) {
            is UrlPhoto -> {
                val dialog = indeterminateProgressDialog("ลบภาพถ่าย").apply { show() }
                storage.delete(Uri.parse(photo.url)) {
                    onComplete {
                        dialog.dismiss()
                        photoList.remove(photo)
                        photos.takePhotoAdapter.update(photoList)
                    }
                    onFail {
                        dialog.dismiss()
                        handle(it)
                    }
                }
            }
            is UriPhoto -> {
                photoList.remove(photo)
                photos.takePhotoAdapter.update(photoList)
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }
}

var Intent.urls: List<String>?
    get() = getStringArrayExtra("urls")?.toList()
    set(value) {
        putExtra("urls", value!!.toTypedArray())
    }

var Intent.limit: Int
    get() = getIntExtra("limit", 2)
    set(value) {
        putExtra("limit", value)
    }

internal var Intent.photoType: PhotoType
    get() = when (getStringExtra("folder")) {
        PhotoType.SERVICE.folder -> PhotoType.SERVICE
        PhotoType.PERSON.folder -> PhotoType.PERSON
        PhotoType.PLACE.folder -> PhotoType.PLACE
        else -> throw IllegalArgumentException("Must specify folder")
    }
    set(value) {
        putExtra("folder", value.folder)
    }

fun Activity.startTakePhotoActivity(
    type: PhotoType,
    urls: List<String>? = null,
    limit: Int = 2,
    requestCode: Int = REQUEST_TAKE_PHOTO
) {
    val intent = intentFor<TakePhotoActivity>()
    urls?.let { intent.urls = it }
    intent.photoType = type
    intent.limit = limit
    startActivityForResult(intent, requestCode)
}
//
fun Fragment.startTakePhotoActivity(
    type: PhotoType,
    urls: List<String>? = null,
    limit: Int = 2,
    requestCode: Int = REQUEST_TAKE_PHOTO
) {
    val intent = context!!.intentFor<TakePhotoActivity>()
    urls?.let { intent.urls = it }
    intent.photoType = type
    intent.limit = limit
    startActivityForResult(intent, requestCode)
}
