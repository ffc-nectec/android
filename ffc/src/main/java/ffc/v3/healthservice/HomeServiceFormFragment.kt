package ffc.v3.healthservice

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ffc.entity.healthcare.CommunityServiceType
import ffc.entity.healthcare.HomeVisit
import ffc.v3.R
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.detailField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.planField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.resultField
import kotlinx.android.synthetic.main.hs_homevisit_from_fragment.syntomField
import me.piruin.spinney.Spinney
import org.jetbrains.anko.support.v4.find

internal class HomeServiceFormFragment : Fragment() {

    val communityServicesField by lazy { find<Spinney<CommunityServiceType>>(R.id.communityServiceField) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_homevisit_from_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        communityServices().all { list, throwable ->
            if (throwable != null) {

            } else {
                communityServicesField.setItemPresenter { item, position ->
                    if (item is CommunityServiceType) {
                        "${item.id} - ${item.name}"
                    } else {
                        item.toString()
                    }
                }
                communityServicesField.setSearchableItem(list)
            }
        }
    }

    fun dataInto(visit: HomeVisit) {
        require(communityServicesField.selectedItem != null)

        visit.apply {
            serviceType = communityServicesField.selectedItem!!
            syntom = syntomField.text.toString()
            detail = detailField.text.toString()
            result = resultField.text.toString()
            plan = planField.text.toString()
        }
    }
}
