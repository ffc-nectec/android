package ffc.app.person.genogram

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.ColorUtils
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import ffc.android.*
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.health.service.healthCareServicesOf
import ffc.app.person.*
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.entity.Person
import ffc.entity.healthcare.HealthCareService
import ffc.entity.update
import kotlinx.android.synthetic.main.activity_person_popup.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class personPopupActivity : FamilyFolderActivity() {

    private lateinit var viewModel: GenogramActivity.ServicesViewModel
    private var CAMERA_REQUEST_CODE=2034;
    lateinit var personViewModel: PersonActivitiy.PersonViewModel
    private var mStorage: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_popup)
        mStorage = FirebaseStorage.getInstance().reference
        val bundle = intent.extras
        var id = bundle?.getString("id","") ?: ""
        var personId = bundle?.getString("personId", "") ?: ""

        val avatarView = findViewById<CircleImageView>(R.id.avatarView);
        avatarView.onClick {
            dispatchTakePictureIntent();
        }
        val buttonPopup = findViewById<Button>(R.id.button_popup)
                buttonPopup.text = "ปิด"
                buttonPopup.setOnClickListener{
                onBackPressed()
            }
        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                this.window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }
        viewModel = viewModel()
        personViewModel = viewModel();
        observe(personViewModel.person){
            if (it != null) {
               bindPerson(it)
            }
        }
        observe(viewModel.services){
            if (it == null || it.isEmpty())
                emptyViewPerson.showEmpty()
            else{
                emptyViewPerson.showContent()
                bind(it)
            }
        }
        observe(viewModel.loading) { loading ->
            if (loading == true && viewModel.services.value.isNullOrEmpty()){
                emptyViewPerson.showLoading()
            }
        }
        observe(viewModel.exception) {
            it?.let { t ->
                emptyViewPerson.error(t).show()

            }
        }
        homeAsUp.onClick {
            finish()
        }
        loadPerson(personId)
        loadHealthcareServices(personId)
    }
    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }
    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500

        // Fade animation for the Popup Window when you press the back button
        linear_layout.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val extras = data.extras
                val imageBitmap = extras["data"] as Bitmap
                avatarView.setImageBitmap(imageBitmap)

                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val myData = baos.toByteArray()
                var filename:String? = genFileName()
                val uploadTask: UploadTask = mStorage?.child("images/profile/" + filename)!!.putBytes(myData)

                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
                builder.setView(dialogView)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        var url:String = downloadUri?.uploadSessionUri.toString()
                        url = url.replace("?name=","/")
                        url = url.split("&")[0]+"?alt=media"
                        Log.d("--- downloadUri ---", url)
                        personViewModel.person.value?.update { avatarUrl = url }?.pushTo(org!!) {
                            onComplete { personViewModel.person.value = it
                                dialog.dismiss()
                            }
                            onFail {
                                personViewModel.exception.value = it
                                dialog.dismiss()
                            }
                        }
                    } else {
                        dialog.dismiss()
                    }
                }
            }
        }
    }
    private fun loadHealthcareServices(personId: String) {
        viewModel.loading.value = true
        var data: List<HealthCareService> = emptyList()

        healthCareServicesOf(personId!!, org!!.id).all {
            always { viewModel.loading.value = false }
            onFound { viewModel.services.value = it.sortedByDescending { it.time } }
            onNotFound { viewModel.services.value = emptyList() }
            onFail { viewModel.exception.value = it }
        }
    }
    private fun loadPerson(personId: String)
    {
        persons(org!!.id).person(personId) {
            onFound { personViewModel.person.value = it }
            onNotFound { personViewModel.person.value = null }
            onFail { personViewModel.exception.value = it }
        }
    }
    private fun bindPerson(data:Person){
        var avatarView = findViewById<CircleImageView>(R.id.avatarView)
        var name = findViewById<TextView>(R.id.nameView)
        var age = findViewById<TextView>(R.id.ageView)
        if(data.avatarUrl!=null) {
            avatarView.load(Uri.parse(data.avatarUrl))
        }
        name.text = data.name
        age.text = data.age.toString()+" ปี"
    }
    private fun bind(data:List<HealthCareService>){
        if(data!=null){
            if(data[0].photosUrl!=null) {
                var uri: Uri = Uri.parse(data[0].photosUrl.toString())
                //avatarView.load(uri)
            }
            if(data[0].ncdScreen!=null){
                var smoke = findViewById<TextView>(R.id.smikingLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black);
                img = drawable(R.drawable.ic_unknown_black)
                smoke.drawableStart = img
                if(data[0].ncdScreen?.smoke!=null){

                    if(data[0].ncdScreen?.smoke.toString()=="NEVER"){
                        img = drawable(R.drawable.ic_level_blue1)
                    }
                    else if(data[0].ncdScreen?.smoke.toString()=="RARELY"){
                        img = drawable(R.drawable.ic_level_blue2)
                    }
                    else if(data[0].ncdScreen?.smoke.toString()=="OCCASIONALLY"){
                        img = drawable(R.drawable.ic_level_blue3)
                    }
                    else if(data[0].ncdScreen?.smoke.toString()=="USUALLY"){
                        img = drawable(R.drawable.ic_level_blue4)
                    }
                    else if(data[0].ncdScreen?.smoke.toString()=="UNKNOWN"){
                        img = drawable(R.drawable.ic_unknown_black)
                    }
                    smoke.drawableStart = img
                }

                if(data[0].ncdScreen?.alcohol!=null){
                    var alcohol = findViewById<TextView>(R.id.dringLevel)
                    var img: Drawable = drawable(R.drawable.ic_level_blue0);
                    img = drawable(R.drawable.ic_unknown_black)
                    if(data[0].ncdScreen?.alcohol.toString()=="NEVER"){
                        img = drawable(R.drawable.ic_level_blue1)
                    }
                    else if(data[0].ncdScreen?.alcohol.toString()=="RARELY"){
                        img = drawable(R.drawable.ic_level_blue2)
                    }
                    else if(data[0].ncdScreen?.alcohol.toString()=="OCCASIONALLY"){
                        img = drawable(R.drawable.ic_level_blue3)
                    }
                    else if(data[0].ncdScreen?.alcohol.toString()=="USUALLY"){
                        img = drawable(R.drawable.ic_level_blue4)
                    }
                    else if(data[0].ncdScreen?.alcohol.toString()=="UNKNOWN"){
                        img = drawable(R.drawable.ic_level_blue0)
                    }
                    alcohol.drawableStart = img
                }
                if(data[0].ncdScreen?.bloodSugar!=null){
                    var alcohol = findViewById<TextView>(R.id.sugarLevel)
                    var img: Drawable = drawable(R.drawable.ic_level_blue0);
                    img = drawable(R.drawable.ic_unknown_black)
                    if(data[0].ncdScreen?.bloodSugar!!>0){

                    }
                }
            }
            else{
                var smoke = findViewById<TextView>(R.id.smikingLevel)
                var alcohol = findViewById<TextView>(R.id.dringLevel)
                var sugar = findViewById<TextView>(R.id.sugarLevel)
                var salt = findViewById<TextView>(R.id.saltLevel)
                var exercise = findViewById<TextView>(R.id.exerciseLevel)
                var buyMedic = findViewById<TextView>(R.id.buyYourOwnMedicienLevel)
                var drinkEnergy = findViewById<TextView>(R.id.energyDrinkLevel)
                var accident = findViewById<TextView>(R.id.accidentLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black);
                smoke.drawableStart = img
                alcohol.drawableStart = img
                sugar.drawableStart = img
                salt.drawableStart = img
                exercise.drawableStart = img
                try {
                    var sizeInDp = 8
                    val scale = resources.displayMetrics.density
                    val dpAsPixels = (sizeInDp * scale + 0.5f)
                    buyMedic.drawableStart = img
                    buyMedic.setPadding(dpAsPixels.toInt(), 0, 0, 0)
                    drinkEnergy.drawableStart = img
                    drinkEnergy.setPadding(dpAsPixels.toInt(), 0, 0, 0)
                    accident.drawableStart = img
                    accident.setPadding(dpAsPixels.toInt(), 0, 0, 0)
                }
                catch(ex: Exception){
                    Toast.makeText(applicationContext,ex.message,Toast.LENGTH_SHORT).show()
                }

            }
            var  tvBMI:TextView = findViewById(R.id.tvBMIResult)
            tvBMI.text="BMI kg/m2 = "+data[0].bmi!!.value+"";
            var descript="";
            if(data.size>0) {
                if (data[0].bmi!!.value < 18.5) {
                    descript = "น้ำหนักตำกว่าเกณฑ์";
                    imgBMI.setImageResource(R.drawable.ic_slim)
//                    val tr = findViewById<TableRow>(R.id.bmi_revel1)
//                    var tv = findViewById<TextView>(R.id.bmi_revel1_cell1)
//                    if (tr != null) {
//                        tr.setBackgroundColor(Color.parseColor("#e74c3c"))
//                        tv.text=tv.text.toString()+" ("+data[0].bmi!!.value+")"
//
//                    }
                }
                if (data[0].bmi!!.value >= 18.5 && data[0].bmi!!.value <= 22.90) {
                    descript = "สมส่วน"
                    imgBMI.setImageResource(R.drawable.ic_weight_loss)
//                    val tr = findViewById<TableRow>(R.id.bmi_revel2)
//                    var tv = findViewById<TextView>(R.id.bmi_revel2_cell1)
//                    if (tr != null) {
//
//                        tr.setBackgroundColor(Color.parseColor("#e67e22"))
//                        tv.text=tv.text.toString()+" ("+data[0].bmi!!.value+")"
//                    }
                }
                if (data[0].bmi!!.value >= 23 && data[0].bmi!!.value <= 24.90) {
                    descript = "น้ำหนักเกิน"
                    imgBMI.setImageResource(R.drawable.ic_fat)
//                    val tr = findViewById<TableRow>(R.id.bmi_revel3)
//                    var tv = findViewById<TextView>(R.id.bmi_revel3_cell1)
//                    if (tr != null) {
//                        tr.setBackgroundColor(Color.parseColor("#2ecc71"))
//                        tv.text=tv.text.toString()+" ("+data[0].bmi!!.value+")"
//                    }
                }
                if (data[0].bmi!!.value >= 25 && data[0].bmi!!.value <= 29.90) {
                    descript = "โรคอ้วน"
                    imgBMI.setImageResource(R.drawable.ic_fat)
//                    val tr = findViewById<TableRow>(R.id.bmi_revel4)
//                    var tv = findViewById<TextView>(R.id.bmi_revel4_cell1)
//                    if (tr != null) {
//                        tr.setBackgroundColor(Color.parseColor("#f1c40f"))
//                        tv.text=tv.text.toString()+" ("+data[0].bmi!!.value+")"
//                    }
                }
                if (data[0].bmi!!.value >= 30) {
                    descript ="โรคอ้วนอันตราย"
                    imgBMI.setImageResource(R.drawable.ic_obesity)
//                    val tr = findViewById<TableRow>(R.id.bmi_revel5)
//                    var tv = findViewById<TextView>(R.id.bmi_revel5_cell1)
//                    if (tr != null) {
//                        tr.setBackgroundColor(Color.parseColor("#f49c13"))
//                        tv.text=tv.text.toString()+" ("+data[0].bmi!!.value+")"
//                    }
                }
            }
            tvBMI.text="BMI kg/m2 = "+data[0].bmi!!.value+" "+descript;
            bloosePressure(data)
            blooseGlucose(data)

        }
    }
    private fun bloosePressure(data:List<HealthCareService>)
    {
        try {
            var chart = findViewById(R.id.chartBloosePressure) as LineChart
            chart.setTouchEnabled(true)
            chart.setPinchZoom(true)
            val xAxis: XAxis = chart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f
            xAxis.enableGridDashedLine(1f, 1f, 0f)
            xAxis.axisMaximum = 5f
            xAxis.axisMinimum = 0f
            xAxis.setDrawLimitLinesBehindData(true)

            val ll1 = LimitLine(120f, "Max")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 12f

            val ll2 = LimitLine(80f, "Min")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 12f
            ll2.lineColor =  ContextCompat.getColor(this, R.color.orange_400)

            val yAxisRight: YAxis = chart.getAxisRight()
            yAxisRight.isEnabled = false

            val yAxisLeft: YAxis = chart.getAxisLeft()
            yAxisLeft.removeAllLimitLines()
//            yAxisLeft.addLimitLine(ll1)
//            yAxisLeft.addLimitLine(ll2)
            yAxisLeft.granularity = 1f
            yAxisLeft.axisMaximum = 200f
            yAxisLeft.axisMinimum = 0f
            yAxisLeft.textSize = 12f
            yAxisLeft.enableGridDashedLine(10f,10f,0f)
            yAxisLeft.setDrawZeroLine(false)
            yAxisLeft.setDrawLimitLinesBehindData(false)

            chart.data = LineData(getDataBloosePressure(data))
            chart.animateX(2500)
            chart.invalidate()
            chart.getAxisRight().setEnabled(false)
        }
        catch (ex:Exception){
            Toast.makeText(applicationContext,ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDataBloosePressure(data:List<HealthCareService>): java.util.ArrayList<ILineDataSet>? {

        var lineDataSets:  java.util.ArrayList<ILineDataSet> = java.util.ArrayList<ILineDataSet>()

        val line1 =
            ArrayList<Entry>()
        val line2 =
            ArrayList<Entry>()
        if(data!=null) {
            if (data.size > 0) {
                if (data[0].bloodPressure != null) {

                    var pSystolic1 = data[0].bloodPressure!!.systolic
                    var pDiastolic1 = data[0].bloodPressure!!.diastolic
                    if (pSystolic1 != null) {
                        line1.add(Entry(1f, pSystolic1.toFloat()))
                        line2.add(Entry(1f, pDiastolic1.toFloat()))
                    }
                }
                if(data.size>1){
                    if (data[1].bloodPressure != null) {
                        var pSystolic2 = data[0].bloodPressure!!.systolic
                        var pDiastolic2 = data[0].bloodPressure!!.diastolic
                        if (pSystolic2 != null) {
                            line1.add(Entry(2f, pSystolic2.toFloat()))
                            line2.add(Entry(2f, pDiastolic2.toFloat()))
                        }
                    }
                }
                var ds1: LineDataSet = LineDataSet(line1, "Systolic")
                ds1.color = ContextCompat.getColor(this, R.color.blue_400)
                ds1.valueTextSize = 12f
                ds1.lineWidth = 4f

                var ds2: LineDataSet = LineDataSet(line2, "Diastolic")
                ds2.color = ContextCompat.getColor(this, R.color.orange_400)
                ds2.valueTextSize = 12f
                ds2.lineWidth = 4f
                lineDataSets.add(ds1)
                lineDataSets.add(ds2)
            }
        }
        return lineDataSets
    }
    private fun blooseGlucose(data:List<HealthCareService>)
    {
        try {
            var chart = findViewById(R.id.chartBlooseGlucose) as LineChart
            chart.setTouchEnabled(true)
            chart.setPinchZoom(true)
            val xAxis: XAxis = chart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f
            xAxis.enableGridDashedLine(1f, 1f, 0f)
            xAxis.axisMaximum = 5f
            xAxis.axisMinimum = 0f
            xAxis.setDrawLimitLinesBehindData(true)

            val ll1 = LimitLine(100f, "Max")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 12f

            val ll2 = LimitLine(70f, "Min")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 12f
            ll2.lineColor =  ContextCompat.getColor(this, R.color.orange_400)

            val yAxisRight: YAxis = chart.getAxisRight()
            yAxisRight.isEnabled = false

            val yAxisLeft: YAxis = chart.getAxisLeft()
            yAxisLeft.removeAllLimitLines()
//            yAxisLeft.addLimitLine(ll1)
//            yAxisLeft.addLimitLine(ll2)
            yAxisLeft.granularity = 1f
            yAxisLeft.axisMaximum = 150f
            yAxisLeft.axisMinimum = 0f
            yAxisLeft.textSize = 12f
            yAxisLeft.enableGridDashedLine(10f,10f,0f)
            yAxisLeft.setDrawZeroLine(false)
            yAxisLeft.setDrawLimitLinesBehindData(false)

            chart.data = LineData(getDataGlucose(data))
            chart.animateX(2500)
            chart.invalidate()
            chart.getAxisRight().setEnabled(false)
        }
        catch (ex:Exception){
            Toast.makeText(applicationContext,ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDataGlucose(data:List<HealthCareService>): java.util.ArrayList<ILineDataSet>? {

        var lineDataSets:  java.util.ArrayList<ILineDataSet> = java.util.ArrayList<ILineDataSet>()

        val line1 =
            ArrayList<Entry>()
        val line2 =
            ArrayList<Entry>()
        if(data!=null) {
            if(data[0].ncdScreen!=null) {
                if (data[0].ncdScreen!!.bloodSugar != null) {
                    var pbloodSugar1 = data[0].ncdScreen!!.bloodSugar
                    if (pbloodSugar1 != null) {
                        line1.add(Entry(1f, pbloodSugar1.toFloat()))
                    }
                }
                var ds1: LineDataSet = LineDataSet(line1, "Glocose")
                ds1.color = ContextCompat.getColor(this, R.color.blue_400)
                ds1.valueTextSize = 12f
                ds1.lineWidth = 4f
                lineDataSets.add(ds1)
            }
        }
        return lineDataSets
    }

    private fun genFileName(): String? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return "JPG_" + timeStamp + "_" + UUID.randomUUID().toString() + ".jpg"
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }
}
