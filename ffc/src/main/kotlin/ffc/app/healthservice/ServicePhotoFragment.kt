package ffc.app.healthservice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.android.onClick
import ffc.app.R
import ffc.app.dev
import ffc.app.photo.PhotoType
import ffc.app.photo.asymmetric.bind
import ffc.app.photo.startTakePhotoActivity
import ffc.app.photo.urls
import ffc.entity.healthcare.HealthCareService
import ffc.entity.util.URLs
import kotlinx.android.synthetic.main.hs_photo_fragment.asymmetricRecyclerView
import kotlinx.android.synthetic.main.hs_photo_fragment.takePhoto

class ServicePhotoFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    var photoUrls: List<String> = listOf()
        set(value) {
            field = value
            asymmetricRecyclerView.bind(value)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_photo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dev {
            photoUrls = listOf(
                "https://upload.wikimedia.org/wikipedia/commons/0/06/Hotel_Wellington_Sherbrooke.jpg",
                "https://c.pxhere.com/photos/b0/71/new_home_for_sale_georgia_usa_home_house_estate_sale-486530.jpg!d"
            )
        }
        with(asymmetricRecyclerView) {
            bind(photoUrls)
        }
        takePhoto.onClick {
            startTakePhotoActivity(PhotoType.SERVICE, photoUrls)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            with(asymmetricRecyclerView) {
                bind(data!!.urls!!)
            }
        }
    }

    override fun dataInto(service: HealthCareService) {
        service.photosUrl = photoUrls.mapTo(URLs()) { it }
    }
}
