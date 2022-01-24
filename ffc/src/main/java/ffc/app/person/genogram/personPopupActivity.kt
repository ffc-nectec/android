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
import android.provider.MediaStore
//import android.support.v4.content.ContextCompat
//import android.support.v4.graphics.ColorUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import ffc.android.*
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.health.service.healthCareServicesOf
import ffc.app.person.PersonActivitiy
import ffc.app.person.persons
import ffc.app.person.pushTo
import ffc.app.photo.REQUEST_TAKE_PHOTO
import ffc.app.util.alert.toast
import ffc.entity.Person
import ffc.entity.healthcare.Frequency
import ffc.entity.healthcare.HealthCareService
import ffc.entity.update
import ffc.genogram.getResourceAs
import kotlinx.android.synthetic.main.activity_person_popup.*
import org.joda.time.DateTime
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class personPopupActivity : FamilyFolderActivity() {

    private lateinit var viewModel: GenogramActivity.ServicesViewModel
    private var CAMERA_REQUEST_CODE=2034;
    lateinit var personViewModel: PersonActivitiy.PersonViewModel
    private var mStorage: StorageReference? = null
    var maxYBloodPressure =0.0f
    var maxYBloodSugar = 0.0f
    var lstDate: ArrayList<String>? =null;
    var lstDateSugar: ArrayList<String>? =null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_popup)
        mStorage = FirebaseStorage.getInstance().reference
        val bundle = intent.extras
        var id = bundle?.getString("id","") ?: ""
        var personId = bundle?.getString("personId", "") ?: ""
        var  tvBMI:TextView = findViewById(R.id.tvBMIResult)
        tvBMI.text = ""
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
        lstDate = ArrayList<String>()
        lstDateSugar  = ArrayList<String>()
        viewModel = viewModel()
        personViewModel = viewModel();
        observe(personViewModel.person){
            if (it == null)
            {
                emptyViewPerson.showEmpty()
            }
            else{
                emptyViewPerson.showContent()
                bindPerson(it)
            }
        }
        observe(viewModel.services){
            if (it != null &&  !it.isEmpty()) {
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
        var drink = findViewById<TextView>(R.id.drink)
        var smoking = findViewById<TextView>(R.id.smoking)
        var sugar = findViewById<TextView>(R.id.sugar)
        var salt = findViewById<TextView>(R.id.salt)
        var exercise = findViewById<TextView>(R.id.exercise)
        var buyYourOwnMedicien = findViewById<TextView>(R.id.buyYourOwnMedicien)
        var energyDrink= findViewById<TextView>(R.id.energyDrink)
        var accident = findViewById<TextView>(R.id.accident)
        drink.onClick {
            Toast.makeText(this,"ดืมสุรา",Toast.LENGTH_LONG).show();
        }
        smoking.onClick {
            Toast.makeText(this,"สูบบุหรี่",Toast.LENGTH_LONG).show();
        }
        sugar.onClick {
            Toast.makeText(this,"ทานหวาน",Toast.LENGTH_LONG).show();
        }
        salt.onClick {
            Toast.makeText(this,"ทานเค็ม",Toast.LENGTH_LONG).show();
        }
        exercise.onClick {
            Toast.makeText(this,"ออกกำลังกาย",Toast.LENGTH_LONG).show();
        }
        buyYourOwnMedicien.onClick {
            Toast.makeText(this,"ซื้อยาทานเอง",Toast.LENGTH_LONG).show();
        }
        energyDrink.onClick {
            Toast.makeText(this,"ดื่มเครื่องดื่มชูกำลัง",Toast.LENGTH_LONG).show();
        }
        accident.onClick {
            Toast.makeText(this,"ประสบอุบัติเหตุใหญ่",Toast.LENGTH_LONG).show();
        }
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
                val imageBitmap = extras?.get("data") as Bitmap
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
//        Log.d("Behavior:","=========================")
//        Log.d("alcohol:",data.behavior!!.alcohol.toString());
//        Log.d("exercise:",data.behavior!!.exercise.toString());
//        Log.d("bigaccidentever:",data.behavior!!.bigaccidentever.toString());
//        Log.d("tonic:",data.behavior!!.tonic.toString());
//        Log.d("drugbyyourseft:",data.behavior!!.drugbyyourseft.toString());
//        Log.d("sugar:",data.behavior!!.sugar.toString());
//        Log.d("salt:",data.behavior!!.salt.toString());
        if(data.behavior!=null){
            var smoke = findViewById<TextView>(R.id.smikingLevel)
            var img: Drawable = drawable(R.drawable.ic_unknown_black)
            img.setBounds(0, 0, 60, 70)
            smoke.setCompoundDrawables(img,null,null,null);
            if(data.behavior!!.smoke!=null){

                if(data.behavior!!.smoke == Frequency.NEVER){
                    img = drawable(R.drawable.speed_level_1)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior!!.smoke == Frequency.RARELY){
                    img = drawable(R.drawable.speed_level_2)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.smoke == Frequency.OCCASIONALLY){
                    img = drawable(R.drawable.speed_level_3)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.smoke == Frequency.USUALLY){
                    img = drawable(R.drawable.speed_level_4)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.smoke == Frequency.UNKNOWN){
                    img = drawable(R.drawable.ic_unknown_black)
                    img.setBounds(0, 0, 60, 60)
                }
                //smoke.background = img
                smoke.setCompoundDrawables(img,null,null,null);
            }
            if(data.behavior?.alcohol!=null){
                var alcohol = findViewById<TextView>(R.id.dringLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                img.setBounds(0, 0, 60, 60)
                //alcohol.background = img
                if(data.behavior?.alcohol == Frequency.NEVER){
                    img = drawable(R.drawable.speed_level_1)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.alcohol == Frequency.RARELY){
                    img = drawable(R.drawable.speed_level_2)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.alcohol ==Frequency.OCCASIONALLY){
                    img = drawable(R.drawable.speed_level_3)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.alcohol ==Frequency.USUALLY){
                    img = drawable(R.drawable.speed_level_4)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.alcohol ==Frequency.UNKNOWN){
                    img = drawable(R.drawable.ic_unknown_black)
                    img.setBounds(0, 0, 60, 60)
                }
                //alcohol.background = img
                alcohol.setCompoundDrawables(img,null,null,null);
            }
            if(data.behavior?.exercise!=null){
                var exercise = findViewById<TextView>(R.id.exerciseLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                img.setBounds(0, 0, 60, 60)
                if(data.behavior?.exercise == Frequency.NEVER){
                    img = drawable(R.drawable.speed_level_4)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.exercise == Frequency.RARELY){
                    img = drawable(R.drawable.speed_level_3)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.exercise == Frequency.OCCASIONALLY){
                    img = drawable(R.drawable.speed_level_2)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.exercise == Frequency.USUALLY){
                    img = drawable(R.drawable.speed_level_1)
                    img.setBounds(0, 0, 100, 70)
                }
                else if(data.behavior?.exercise == Frequency.UNKNOWN){
                    img = drawable(R.drawable.ic_unknown_black)
                    img.setBounds(0, 0, 60, 60)
                }
                //exercise.background = img
                exercise.setCompoundDrawables(img,null,null,null);
            }
            if(data.behavior?.bigaccidentever!=null){
                var bigaccidentever = findViewById<TextView>(R.id.accidentLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                if(data.behavior?.bigaccidentever==true){
                    var img: Drawable =drawable(R.drawable.ic_yes_black)
                    img.setBounds(0, 0, 60, 60)
                    bigaccidentever.setCompoundDrawables(img,null,null,null);
                }else{
                    img = drawable(R.drawable.ic_no_black)
                    img.setBounds(0, 0, 60, 60)
                    bigaccidentever.setCompoundDrawables(img,null,null,null);
                }
            }
            if(data.behavior?.tonic!=null){
                var tonic = findViewById<TextView>(R.id.energyDrinkLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                if(data.behavior?.tonic==true){
                    img = drawable(R.drawable.ic_yes_black)
                    img.setBounds(0, 0, 60, 60)
                    tonic.setCompoundDrawables(img,null,null,null);
                }else{
                    img = drawable(R.drawable.ic_no_black)
                    img.setBounds(0, 0, 60, 60)
                    tonic.setCompoundDrawables (img,null,null,null);
                }
            }
            if(data.behavior?.drugbyyourseft!=null){
                var drugbyyourseft = findViewById<TextView>(R.id.buyYourOwnMedicienLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                if(data.behavior?.drugbyyourseft==true){
                    img = drawable(R.drawable.ic_yes_black)
                    img.setBounds(0, 0, 60, 60)
                    drugbyyourseft.setCompoundDrawables(img,null,null,null);

                }else{
                    img = drawable(R.drawable.ic_no_black)
                    img.setBounds(0, 0, 60, 60)
                    drugbyyourseft.setCompoundDrawables(img,null,null,null);
                }
            }
            if(data.behavior?.sugar!=null){
                var sugarLevel = findViewById<TextView>(R.id.sugarLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                if(data.behavior?.sugar==true){
                    img = drawable(R.drawable.ic_yes_black)
                    img.setBounds(0, 0, 60, 60)
                    sugarLevel.setCompoundDrawables(img,null,null,null);

                }else{
                    img = drawable(R.drawable.ic_no_black)
                    img.setBounds(0, 0, 60, 60)
                    sugarLevel.setCompoundDrawables(img,null,null,null);
                }
            }
            if(data.behavior?.salt!=null){
                var saltLevel = findViewById<TextView>(R.id.saltLevel)
                var img: Drawable = drawable(R.drawable.ic_unknown_black)
                if(data.behavior?.salt==true){
                    img = drawable(R.drawable.ic_yes_black)
                    img.setBounds(0, 0, 60, 60)
                    saltLevel.setCompoundDrawables(img,null,null,null);
                }else{
                    img = drawable(R.drawable.ic_no_black)
                    img.setBounds(0, 0, 60, 60)
                    saltLevel.setCompoundDrawables(img,null,null,null);
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
            smoke.background = img
            alcohol.background = img
            sugar.background = img
            salt.background = img
            exercise.background = img
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
    }
    private fun bind(data:List<HealthCareService>){
        if(data!=null){
//            if(data[0].photosUrl!=null) {
//                var uri: Uri = Uri.parse(data[0].photosUrl.toString())
//                avatarView.load(uri)
//            }
            var  tvBMI:TextView = findViewById(R.id.tvBMIResult)
            var tvBMIMessage = findViewById<TextView>(R.id.bmiMessage)
            tvBMI.text="";
            var descript="";
            var imgBMI = findViewById<ImageView>(R.id.imgBMI);
            if(data.size>0) {
                if (data[0].bmi != null)
                {
                    if (data[0].bmi!!.value < 18.5) {
                        descript = "น้ำหนักตำกว่าเกณฑ์";
                        imgBMI.setImageResource(R.drawable.bmi_1)
                        tvBMIMessage.setTextColor(getResources().getColor(R.color.green_100))
                    }
                    if (data[0].bmi!!.value >= 18.5 && data[0].bmi!!.value <= 22.90) {
                        descript = "สมส่วน"
                        imgBMI.setBackgroundResource(R.drawable.bmi_2)
                        tvBMIMessage.setTextColor(getResources().getColor(R.color.green_500))
                    }
                    if (data[0].bmi!!.value >= 23 && data[0].bmi!!.value <= 24.90) {
                        descript = "น้ำหนักเกิน"
                        imgBMI.setBackgroundResource(R.drawable.bmi_3)
                        tvBMIMessage.setTextColor(getResources().getColor(R.color.yellow_700))
                    }
                    if (data[0].bmi!!.value >= 25 && data[0].bmi!!.value <= 29.90) {
                        descript = "โรคอ้วน"
                        imgBMI.setBackgroundResource(R.drawable.bmi_4)
                        tvBMIMessage.setTextColor(getResources().getColor(R.color.orange_500))
                    }
                    if (data[0].bmi!!.value >= 30) {
                        descript = "โรคอ้วนอันตราย"
                        imgBMI.setBackgroundResource(R.drawable.bmi_5)
                        tvBMIMessage.setTextColor(getResources().getColor(R.color.red_500))
                    }
                    val output = DateFormat(data[0].time)
                    var date = "\nข้อมูลเมื่อวันที่: "+output;
                    tvBMI.text = "BMI (" + data[0].weight + "/" + (data[0].height?.div(100)) + "^2) kg/m2 = " + data[0].bmi!!.value +date;
                    tvBMIMessage.text = descript
                    tvBMIMessage.textSize= 16F


                }
                else
                {
                    tvBMI.text = "ไม่มีข้อมูล BMI";
                }
            }
            bloodPressure(data)
            bloodSugar(data)

        }
    }
    private fun bloodPressure(data:List<HealthCareService>)
    {
        try {
            var myLineData = LineData(getDataBloodPressure(data))
            var chart = findViewById(R.id.chartBloosePressure) as LineChart
            chart.setTouchEnabled(true)
            chart.setPinchZoom(true)

            // enable touch gestures
            chart.setTouchEnabled(true)

            val xAxis: XAxis = chart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f
            xAxis.enableGridDashedLine(1f, 1f, 0f)
           // xAxis.axisMaximum = lstDate!!.size.toFloat();
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f;
            xAxis.setDrawLimitLinesBehindData(true)
            val l: Legend = chart.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(true)
            //l.typeface = tfLight
            l.yOffset = 0f
            l.xOffset = 10f
            l.yEntrySpace = 0f
            l.textSize = 8f

            val ll1 = LimitLine(120f, "120")
            ll1.lineWidth = 1f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 12f
            ll1.lineColor =  ContextCompat.getColor(this,R.color.systolic_pressure)

            val ll2 = LimitLine(80f, "80")
            ll2.lineWidth = 1f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            ll2.textSize = 12f
            ll2.lineColor =  ContextCompat.getColor(this, R.color.diastolic_pressure)

            val yAxisRight: YAxis = chart.getAxisRight()
            yAxisRight.isEnabled = false

            val yAxisLeft: YAxis = chart.getAxisLeft()
            yAxisLeft.removeAllLimitLines()

            yAxisLeft.addLimitLine(ll1)
            yAxisLeft.addLimitLine(ll2)

            yAxisLeft.granularity = 1f
            yAxisLeft.axisMaximum = maxYBloodPressure
            yAxisLeft.axisMinimum = 0f
            yAxisLeft.textSize = 12f
            yAxisLeft.enableGridDashedLine(10f,10f,0f)
            yAxisLeft.setDrawZeroLine(false)
            yAxisLeft.setDrawLimitLinesBehindData(false)

            // create marker to display box when values are selected
            val mv = MyMarkerView(this,R.layout.custom_marker_view);
            mv.lstDate = lstDate;
            chart.marker = mv
            mv.chartView = chart
            mv.chartType = "Line"
            chart.data = myLineData
            chart.animateX(2500)
            chart.invalidate()
            chart.getAxisRight().setEnabled(false)
        }
        catch (ex:Exception){
            Toast.makeText(applicationContext,ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDataBloodPressure(data:List<HealthCareService>): java.util.ArrayList<ILineDataSet>? {

        var lineDataSets:  java.util.ArrayList<ILineDataSet> = java.util.ArrayList<ILineDataSet>()

        val line1 =
            ArrayList<Entry>()
        val line2 =
            ArrayList<Entry>()
        if(data!=null) {
            if (data.size > 0) {
                var icount = 0;
                for(i in data.size-1 downTo 0)
                {
                    if (data[i].bloodPressure != null) {
                        if(maxYBloodPressure<data[i].bloodPressure!!.systolic){
                            maxYBloodPressure = data[i].bloodPressure!!.systolic.toFloat() +50f;
                        }
                        var pSystolic1 = data[i].bloodPressure!!.systolic
                        var pDiastolic1 = data[i].bloodPressure!!.diastolic
                        if (pSystolic1 != null) {
                            line1.add(Entry((icount ).toFloat(), pSystolic1.toString().split(".")[0].toFloat()))
                            line2.add(Entry((icount ).toFloat(), pDiastolic1.toString().split(".")[0].toFloat()))
                            lstDate!!.add(DateFormat(data[i].time));
                            icount = icount + 1;
                        }
                    }
                }

                var ds1: LineDataSet = LineDataSet(line1, "Systolic")
                ds1.color = ContextCompat.getColor(this, R.color.systolic_pressure)
                ds1.valueTextSize = 12f
                ds1.lineWidth = 4f

                var ds2: LineDataSet = LineDataSet(line2, "Diastolic")
                ds2.color = ContextCompat.getColor(this, R.color.diastolic_pressure)
                ds2.valueTextSize = 12f
                ds2.lineWidth = 4f
                lineDataSets.add(ds1)
                lineDataSets.add(ds2)
            }
        }
        return lineDataSets
    }
    private fun DateFormat(date: DateTime ):String{
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val output = formatter.format(parser.parse(date.toString()))
        return output;
    }
    private fun bloodSugar(data:List<HealthCareService>)
    {
        try {
            var myBarData = BarData(getDataSugar(data))
            var chart = findViewById(R.id.chartBloodSugar) as BarChart
            chart.setTouchEnabled(true)
            chart.setPinchZoom(true)
            val xAxis: XAxis = chart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f
            xAxis.enableGridDashedLine(1f, 1f, 0f)
            //xAxis.axisMaximum = 5f
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f
            xAxis.setDrawLimitLinesBehindData(true)

            val l: Legend = chart.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(true)
            l.yOffset = 0f
            l.xOffset = 10f
            l.yEntrySpace = 0f
            l.textSize = 8f

            val ll1 = LimitLine(100f, "100")
            ll1.lineWidth = 1f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 12f
            ll1.lineColor =  ContextCompat.getColor(this, R.color.systolic_pressure)

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
            yAxisLeft.addLimitLine(ll1)
//            yAxisLeft.addLimitLine(ll2)
            yAxisLeft.granularity = 1f
            yAxisLeft.axisMaximum = maxYBloodSugar
            yAxisLeft.axisMinimum = 0f
            yAxisLeft.textSize = 12f
            yAxisLeft.enableGridDashedLine(10f,10f,0f)
            yAxisLeft.setDrawZeroLine(false)
            yAxisLeft.setDrawLimitLinesBehindData(false)

            chart.data = myBarData
            val mv = MyMarkerView(this,R.layout.custom_marker_view);
            mv.lstDate = lstDateSugar;
            mv.chartType = "Bar";
            chart.marker = mv
            mv.chartView = chart
            chart.animateX(2500)
            chart.invalidate()
            chart.getAxisRight().setEnabled(false)

        }
        catch (ex:Exception){
            Toast.makeText(applicationContext,ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDataSugar(data:List<HealthCareService>): java.util.ArrayList<IBarDataSet>? {

        var lineDataSets:  java.util.ArrayList<IBarDataSet> = java.util.ArrayList<IBarDataSet>()

        val line1 =
            ArrayList<BarEntry>()
        val line2 =
            ArrayList<BarEntry>()
        var lstColor = ArrayList<Int>()
        if(data!=null) {
            if(data.size>0) {
                var icount = 0;
                for(i in data.size-1 downTo 0)
                {
                    if (data[i].sugarLab != null) {
                        var sugar = data[i].sugarLab
                            icount = icount + 1;
                            line1.add(BarEntry((icount ).toFloat(), sugar!!.toFloat()))
                            if(maxYBloodSugar<sugar!!.toFloat()){
                                maxYBloodSugar = sugar!!.toFloat()+20;
                            }
                            if(sugar>100){
                                lstColor.add(Color.rgb(255, 52, 51));
                            }else {
                                lstColor.add(Color.rgb(102, 204, 0));
                            }
                            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                            val output = formatter.format(parser.parse(data[i].time.toString()))
                            lstDateSugar!!.add(output);
                    }
                }
                var ds1: BarDataSet = BarDataSet(line1, "Blood Sugar")
                    //ds1.color = ContextCompat.getColor(this, R.color.blue_400)
                    ds1.valueTextSize = 12f
                    ds1.setColors(lstColor);
                    //ds1.lineWidth = 4f
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

private fun <E> ArrayList<E>.add(element: Int) {

}

