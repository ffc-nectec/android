package ffc.app

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ffc.android.onClick

import kotlinx.android.synthetic.main.activity_asm_menu.*

class AsmMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asm_menu)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var homeAsUp = findViewById<ImageButton>(R.id.homeAsUp)
        homeAsUp.onClick {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("ยืนยัน")
            builder.setTitle("คุณต้องการออกจากระบบ หรือไม่")
            builder.setPositiveButton(android.R.string.yes){ dialog, which ->
                finish()
            }
            builder.setNegativeButton(android.R.string.no){ dialog, which ->

            }
            builder.show()
        }
    }
}
