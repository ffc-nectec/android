package ffc.app.search

import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ffc.android.addVeriticalItemDivider
import ffc.android.drawable
import ffc.android.drawableStart
import ffc.android.gone
import ffc.android.observe
import ffc.android.onClick
import ffc.android.sceneTransition
import ffc.android.viewModel
import ffc.android.visible
import ffc.app.R
import ffc.app.familyFolderActivity
import ffc.app.location.HouseActivity
import ffc.app.location.HouseAdapter
import ffc.app.person.PersonAdapter
import ffc.app.person.startPersonActivityOf
import ffc.app.util.SimpleViewModel
import ffc.entity.Entity
import ffc.entity.Person
import ffc.entity.place.House
import kotlinx.android.synthetic.main.search_result_fragment.emptyView
import kotlinx.android.synthetic.main.search_result_fragment.header
import kotlinx.android.synthetic.main.search_result_fragment.moreButton
import kotlinx.android.synthetic.main.search_result_fragment.recycleView
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor

abstract class SearchResultFragment<T : Entity> : Fragment() {

    val viewModel by lazy { viewModel<SimpleViewModel<List<T>>>() }

    open val limit = 4
    open var query: String? = null
        set(value) {
            field = value
            value?.let { onSearchFor(viewModel, it) }
        }
    abstract val title: String
    abstract val iconRes: Int
    var containerId: Int = R.id.contentContainer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe(viewModel.content) {
            if (it.isNullOrEmpty()) {
                onBindAdapter(recycleView, listOf())
                emptyView.showEmpty()
                moreButton.gone()
            } else {
                onBindAdapter(recycleView, it)
                emptyView.showContent()
                if (it.size > limit)
                    moreButton.visible()
                else
                    moreButton.gone()
            }
        }
        observe(viewModel.loading) { if (it == true) emptyView.showLoading() }
        observe(viewModel.exception) { it?.let { emptyView.error(it).show() } }

        header.text = title
        header.drawableStart = drawable(iconRes)
        moreButton.onClick {
            activity?.let {
                val fragment = onCreateMoreResultFragment(viewModel.content.value!!)
                it.supportFragmentManager.beginTransaction()
                    .hide(it.supportFragmentManager.findFragmentByTag("result")!!)
                    .add(containerId, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        query?.let { onSearchFor(viewModel, it) }
    }

    abstract fun onSearchFor(viewModel: SimpleViewModel<List<T>>, query: String)
    abstract fun onBindAdapter(recycleView: RecyclerView, content: List<T>)
    abstract fun onCreateMoreResultFragment(content: List<T>): Fragment
}

open class PersonSearchResultFragment : SearchResultFragment<Person>() {

    override val iconRes: Int
        get() = R.drawable.ic_account_circle_black_24dp

    override val title: String
        get() = "คน"

    override fun onSearchFor(viewModel: SimpleViewModel<List<Person>>, query: String) {
        viewModel.loading.value = true
        personSearcher(familyFolderActivity.org!!.id).search(query) {
            always { viewModel.loading.value = false }
            onFound { viewModel.content.value = it }
            onNotFound { viewModel.content.value = listOf() }
            onFail { viewModel.exception.value = it }
        }
    }

    override fun onBindAdapter(recycleView: RecyclerView, content: List<Person>) {
        with(recycleView) {
            layoutManager = LinearLayoutManager(requireContext())
            addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
            adapter = PersonAdapter(content, limit = limit) {
                onItemClick { p ->
                    activity?.startPersonActivityOf(p, null,
                        find<ImageView>(R.id.personImageView) to getString(R.string.transition_person_profile))
                }
            }
        }
    }

    override fun onCreateMoreResultFragment(content: List<Person>): Fragment {
        return PersonMoreResultFragment().apply {
            setArgument(this@PersonSearchResultFragment.query!!, content)
        }
    }
}

open class HouseSearchResultFragment : SearchResultFragment<House>() {
    override val iconRes: Int
        get() = R.drawable.ic_home_black_24px
    override val title: String
        get() = "บ้าน"

    override fun onSearchFor(viewModel: SimpleViewModel<List<House>>, query: String) {
        viewModel.loading.value = true
        houseSearcher(familyFolderActivity.org!!.id).search(query) {
            always { viewModel.loading.value = false }
            onFound { viewModel.content.value = it }
            onNotFound { viewModel.content.value = listOf() }
            onFail { viewModel.exception.value = it }
        }
    }

    override fun onBindAdapter(recycleView: RecyclerView, content: List<House>) {
        with(recycleView) {
            layoutManager = LinearLayoutManager(context)
            addVeriticalItemDivider(dimen(R.dimen.content_start_horizontal_padding))
            adapter = HouseAdapter(content, limit = limit) {
                val intent = requireContext().intentFor<HouseActivity>("houseId" to it.id)
                startActivity(intent, activity?.sceneTransition())
            }
        }
    }

    override fun onCreateMoreResultFragment(content: List<House>): Fragment {
        return HouseMoreResultFragment().apply {
            setArgument(this@HouseSearchResultFragment.query!!, content)
        }
    }
}
