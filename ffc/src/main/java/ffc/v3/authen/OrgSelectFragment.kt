package ffc.v3.authen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.Organization
import ffc.v3.BuildConfig
import ffc.v3.R
import kotlinx.android.synthetic.main.auth_fragment_org_select.nextView
import kotlinx.android.synthetic.main.auth_fragment_org_select.orgView

class OrgSelectFragment : Fragment() {

    lateinit var onNext: ((Organization) -> Unit)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auth_fragment_org_select, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Debug User Login
        if (BuildConfig.DEBUG) {
            nextView.setOnLongClickListener {
                orgView.setText("รพ.สต. เนคเทค")
                true
            }
        }

        nextView.setOnClickListener {
            onNext(getOrg())
        }
    }

    private fun getOrg(): Organization {
        return Organization()
    }
}
