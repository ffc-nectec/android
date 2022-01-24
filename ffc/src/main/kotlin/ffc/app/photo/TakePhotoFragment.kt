package ffc.app.photo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ffc.android.onClick
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.mockRepository
import ffc.entity.healthcare.HealthCareService
import ffc.entity.util.URLs
import kotlinx.android.synthetic.main.hs_photo_fragment.counterView
import kotlinx.android.synthetic.main.hs_photo_fragment.takePhoto

class TakePhotoFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    private val maxPhoto = 2
    private var photoUrls: List<String> = listOf()
        set(value) {
            field = value
            counterView.text = "${field.size}/$maxPhoto"
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_photo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        counterView.text = "${photoUrls.size}/$maxPhoto"

        if (mockRepository) {
            photoUrls = listOf(
                "https://upload.wikimedia.org/wikipedia/commons/0/06/Hotel_Wellington_Sherbrooke.jpg",
                "https://c.pxhere.com/photos/b0/71/new_home_for_sale_georgia_usa_home_house_estate_sale-486530.jpg!d"
            )
        }

        takePhoto.onClick {
            startTakePhotoActivity(PhotoType.SERVICE, photoUrls, maxPhoto)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            photoUrls = data!!.urls!!
    }

    override fun bind(service: HealthCareService) {
        photoUrls = service.photosUrl
    }

    override fun dataInto(service: HealthCareService) {
        service.photosUrl = photoUrls.mapTo(URLs()) { it }
    }
}
