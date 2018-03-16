package v3.ffc.`in`.th.ffc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.password_layout
import kotlinx.android.synthetic.main.activity_login.submit
import kotlinx.android.synthetic.main.activity_login.username_layout
import me.piruin.spinney.Spinney

class LoginActivity : AppCompatActivity() {

  val organization by lazy { findViewById<Spinney<Organization>>(R.id.org) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    organization.setItems(arrayListOf(Organization(1, "รพสกต.เนคเทค")))
    organization.setItemPresenter { item, position -> (item as Organization).name }
    organization.setOnItemSelectedListener { _, selectedItem, _ ->
      username_layout.visible()
      password_layout.visible()
      submit.visible()
    }

    username_layout.gone()
    password_layout.gone()
    submit.gone()
  }
}

data class Organization(
  val id: Long,
  val name: String
)

fun View.gone() {
  this.visibility = View.GONE
}

fun View.visible() {
  this.visibility = View.VISIBLE
}
