package ffc.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//import android.support.v7.app.AppCompatActivity

class MapActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }
    companion object {
        val LATITUDE = "lat"
        val LONGITUDE = "lon"
        val LOCATION = "location"
    }
}
