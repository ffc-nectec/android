package ffc.app.report

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.* // ktlint-disable
import ffc.android.onClick
import ffc.app.MapActivity
import ffc.app.R
import kotlinx.android.synthetic.main.activity_incident_report.*
import org.jetbrains.anko.intentFor

class IncidentReportActivity : AppCompatActivity() {

    internal lateinit var sp: Spinner
    var SHARE_LOCATION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_report)
        addEvent()
        loadSpinner()
    }
    private fun loadSpinner() {
        sp = findViewById(R.id.spinner)
        val category = arrayOf("เฝ้าระวังโควิด-19")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, category)
        sp.adapter = adapter
    }
    private fun addEvent() {
        var homeAsUp: ImageButton = findViewById(R.id.homeAsUp)
        homeAsUp.onClick {
            finish()
        }
        var tvLocation: TextView = findViewById(R.id.tvLocation)
        tvLocation.onClick {

            val intent = intentFor<MapActivity>()
            startActivityForResult(intent, SHARE_LOCATION_REQUEST_CODE)
        }
        imgDelete.onClick {
            tvLocation.text = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SHARE_LOCATION_REQUEST_CODE) {
                val lat = data!!.getDoubleExtra("lat", 0.0)
                val lon = data.getDoubleExtra("lon", 0.0)
                val location = data.getStringExtra("location")
                tvLocation.setText(location)
            }
        }
    }
}
