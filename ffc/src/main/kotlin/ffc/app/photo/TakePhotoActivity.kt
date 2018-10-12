package ffc.app.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import ffc.android.SpacesItemDecoration
import ffc.android.load
import ffc.android.sceneTransition
import ffc.android.toast
import ffc.app.FamilyFolderActivity
import ffc.app.R
import kotlinx.android.synthetic.main.activity_photo_take.photos
import org.jetbrains.anko.dip
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.toast

class TakePhotoActivity : FamilyFolderActivity() {

    val reqPhotoCode = 1928

    var photoList = mutableListOf<Photo>()

    private val maxPhotoSize = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_take)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.urls?.forEach {
            photoList.add(UrlPhoto(it))
        }
        if (photoList.isEmpty()) {
            targetImageUri = takePhoto(reqPhotoCode)
        }

        with(photos) {
            setItemViewCacheSize(2)
            addItemDecoration(SpacesItemDecoration(dip(16)))
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, true)

            adapter = TakePhotoAdapter(photoList) {
                onViewClick { view, photo ->
                    when (view.id) {
                        R.id.image -> viewPhoto(photo.uri, sceneTransition(view to getString(R.string.transition_photo)))
                        R.id.delBtn -> removeImage(photo)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.house_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val dialog = indeterminateProgressDialog("บันทึกภาพถ่าย").apply { show() }
                photoList.upload {
                    dialog.dismiss()
                    val intent = Intent().apply {
                        urls = it
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            R.id.photoMenu -> {
                targetImageUri = takePhoto(reqPhotoCode)
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
                            toast(it)
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

    private var targetImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqPhotoCode) {
            onTakePhotoResult(resultCode)
        }
    }

    private fun onTakePhotoResult(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (photoList.size == maxPhotoSize)
                    photoList.removeAt(0)
                photoList.add(UriPhoto(targetImageUri!!))
                photos.takePhotoAdapter.update(photoList)
            }
        }
    }

    private val storage: PhotoStorage
        get() = org!!.photoStorageFor(intent.photoType)

    private val RecyclerView.takePhotoAdapter: TakePhotoAdapter
        get() = adapter as TakePhotoAdapter

    private fun removeImage(photo: Photo) {
        when (photo) {
            is UrlPhoto -> storage.delete(Uri.parse(photo.url)) {
                onComplete {
                    photoList.remove(photo)
                    photos.takePhotoAdapter.update(photoList)
                }
                onFail { toast(it) }
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

    interface Photo {
        val uri: Uri

        fun showOn(imageView: ImageView) {
            imageView.context.toast("uri $uri")
            imageView.load(uri)
        }
    }

    class UrlPhoto(val url: String) : Photo {
        override val uri: Uri get() = Uri.parse(url)
    }

    class UriPhoto(override val uri: Uri) : Photo
}

var Intent.urls: List<String>?
    get() = getStringArrayExtra("urls")?.toList()
    set(value) {
        putExtra("urls", value!!.toTypedArray())
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
    requestCode: Int = REQUEST_TAKE_PHOTO
) {
    val intent = intentFor<TakePhotoActivity>()
    urls?.let { intent.urls = it }
    intent.photoType = type
    startActivityForResult(intent, requestCode)
}

fun Fragment.startTakePhotoActivity(
    type: PhotoType,
    urls: List<String>? = null,
    requestCode: Int = REQUEST_TAKE_PHOTO
) {
    val intent = intentFor<TakePhotoActivity>()
    urls?.let { intent.urls = it }
    intent.photoType = type
    startActivityForResult(intent, requestCode)
}

val REQUEST_TAKE_PHOTO = 2034
