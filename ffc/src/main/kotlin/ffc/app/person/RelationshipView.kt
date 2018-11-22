package ffc.app.person

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import ffc.app.R
import ffc.entity.Person
import kotlinx.android.synthetic.main.person_list_item.view.personDeadLabel
import kotlinx.android.synthetic.main.person_list_item.view.personNameView

class RelationshipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    init {
        inflate(context, R.layout.person_list_item, this)
    }

    var relationship: Person.Relationship? = null
        set(value) {
            field = value
            value?.let { bind(it) }
        }

    private fun bind(relation: Person.Relationship) {
        with(relation) {
            personDeadLabel.text = when (relate) {
                Person.Relate.Father -> "พ่อ"
                Person.Relate.Mother -> "แม่"
                Person.Relate.Married -> "คู่สมรส"
                Person.Relate.Sibling -> "พี่น้อง"
                Person.Relate.Child -> "ลูก"
                else -> null
            }
            personNameView.text = name
            //avatarUrl?.let { personAvatar.load(Uri.parse(it)) }
            //age?.let { personAge.text = it }
        }
    }
}
