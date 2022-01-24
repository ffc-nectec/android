package ffc.app.person

import android.content.Context
//import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ffc.app.R
import ffc.entity.Person
import org.jetbrains.anko.find

class RelationshipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val labelView: TextView
    private val nameView: TextView
    private val ageView: TextView
    private val avatarView: ImageView

    init {
        inflate(context, R.layout.person_list_item_small, this)
        labelView = find(R.id.personLabel)
        nameView = find(R.id.personNameView)
        ageView = find(R.id.personAgeView)
        avatarView = find(R.id.personImageView)
    }

    var relationship: Person.Relationship? = null
        set(value) {
            field = value
            value?.let { bind(it) }
        }

    private fun bind(relation: Person.Relationship) {
        with(relation) {
            labelView.text = when (relate) {
                Person.Relate.Father -> "พ่อ"
                Person.Relate.Mother -> "แม่"
                Person.Relate.Married -> "คู่สมรส"
                Person.Relate.Sibling -> "พี่น้อง"
                Person.Relate.Child -> "ลูก"
                else -> null
            }
            nameView.text = name
        }
    }
}
