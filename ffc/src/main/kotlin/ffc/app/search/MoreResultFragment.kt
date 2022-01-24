package ffc.app.search

import android.os.Bundle
import ffc.android.getAs
import ffc.android.requestScroll
import ffc.entity.Entity
import ffc.entity.Person
import ffc.entity.gson.toJson
import ffc.entity.place.House
import kotlinx.android.synthetic.main.search_result_fragment.header
import org.jetbrains.anko.bundleOf

interface MoreResultFragment<T : Entity> {

    fun setArgument(query: String, result: List<T>)
}

class PersonMoreResultFragment : PersonSearchResultFragment(), MoreResultFragment<Person> {

    override val limit: Int = Int.MAX_VALUE

    override val title: String
        get() = """คน "${requireArguments().getString("query")}" """.trim()

    val result: List<Person>
        get() = requireArguments().getAs("result")!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.content.value = result
    }

    override fun setArgument(query: String, result: List<Person>) {
        arguments = bundleOf(
            "query" to query,
            "result" to result.toJson()
        )
    }
}

class HouseMoreResultFragment : HouseSearchResultFragment(), MoreResultFragment<House> {

    override val limit: Int = Int.MAX_VALUE

    override val title: String
        get() = """บ้าน "${requireArguments().getString("query")}" """.trim()

    val result: List<House>
        get() = requireArguments().getAs("result")!!

    override fun setArgument(query: String, result: List<House>) {
        arguments = bundleOf(
            "query" to query,
            "result" to result.toJson()
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.content.value = result
        header.requestScroll()
    }
}
