package ffc.app.person.genogram

import android.animation.ObjectAnimator
import android.app.AlertDialog
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle

import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ffc.android.onClick
import ffc.app.FamilyFolderActivity
import ffc.app.R
import ffc.app.auth.auth
import ffc.app.person.personId
import ffc.app.util.alert.handle
import ffc.entity.Organization
import ffc.entity.User
import ffc.entity.gson.toJson
import ffc.entity.healthcare.HealthCareService
import kotlinx.android.synthetic.main.activity_genogram.*
import kotlinx.android.synthetic.main.activity_genogram.emptyViewPerson
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class GenogramActivity : FamilyFolderActivity() {


    private lateinit var viewModel: GenogramActivity.ServicesViewModel
    var pass:Boolean = false
    var maxScrollX = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genogram)
        var tverrorMessage = root_layout.findViewById<TextView>(R.id.errorMessage)
        tverrorMessage.visibility =View.INVISIBLE
        //loadData()
        addEvent();
    }
    private fun addEvent()
    {
        var  homeAsUp :ImageButton = findViewById(R.id.homeAsUp)
        homeAsUp.onClick {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    fun loadData()
    {
        val personId = intent.personId
        val genogram = ApiFamilies(org!!, personId!!)
        emptyViewPerson.showLoading();
        genogram.genogramCollects {
            onSuccess {
                Log.d("genogram:",personId+" -> "+it.toJson());
                loadGenogram(personId, it.toJson(),org!!);
            }
            onFail {
                emptyViewPerson.showEmpty();
                handle(it!!)
            }
        }
    }

    fun loadGenogram(personId:String, jsonData: String, org: Organization){

        val myWebView: WebView = findViewById(R.id.webview)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        myWebView.loadUrl("file:///android_asset/index.html")
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.builtInZoomControls = true
        myWebView.settings.useWideViewPort = true
        myWebView.settings.loadWithOverviewMode = true
        myWebView.scrollBarStyle= WebView.SCROLLBARS_OUTSIDE_OVERLAY
        myWebView.isScrollbarFadingEnabled = true
        myWebView.setPadding(0,0,0,0)
        myWebView.webChromeClient = WebChromeClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myWebView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                run {
//                    if(scrollX!=100000 && scrollX>0) {
//                        maxScrollX = scrollX;
//                    }
                    if(oldScrollX>scrollX && scrollX !=0)
                    {
                        maxScrollX = scrollX
                    }
                    //maxScrollX = scrollX;
                    var oldX = oldScrollX
                    Log.d("=======:","scaleX-->"+v.scaleX+"--> scaleY:"+v.scaleY+" --> scrollX:"+scrollX!!.toString()+" maxScrollX:"+maxScrollX);
                }
            }

        }

        var intent =  Intent(this, personPopupActivity::class.java)
        myWebView.addJavascriptInterface(WebAppInterface(this,root_layout,org!!,intent), "Android")
        myWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    var Scroll= Point(0,0);
                    var scale = 0
                    var size = 0.0

                    val objJSON = JSONArray(jsonData)
                    val jsString = "javascript:initialize(" +
                        "'" + personId + "'," + objJSON + ","+width+","+height+")"
                    view.loadUrl(jsString)
                    view.evaluateJavascript("getWidthContent()") {
                        var orientation = getResources().getConfiguration().orientation;
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            //view.setInitialScale(100);
                            view.setInitialScale(50);
                            // In landscape
                        } else {
                            // In portrait
                            size = it.toDouble()*1.25
                            if(size<600){
                                size=600.0;
                            }
                            scale = getScale(size)
                            view.setInitialScale(scale)
                            view.visibility = View.INVISIBLE
                            view.evaluateJavascript("getFirstPosition()"){
                                //view.scrollX = 100000
                                var anim = ObjectAnimator.ofInt(view, "scrollX",
                                    0, 100000000);
                                /*getScale(it.toDouble())+(scale*1.715).toInt());*/
                                anim.setDuration(500);
                                anim.start();

                                //view.scrollBy(getScale(it.toDouble())+(scale*1.715).toInt(),0)
                            }
                            view.postDelayed(Runnable() {
                                run() {

                                    view.evaluateJavascript("getFirstPosition()"){
                                        //view.scrollX = 100000
                                        view.visibility = View.VISIBLE
                                        var anim = ObjectAnimator.ofInt(view, "scrollX",
                                            0, maxScrollX/2);
                                        /*getScale(it.toDouble())+(scale*1.715).toInt());*/
                                        anim.setDuration(500);
                                        anim.start();

                                        //view.scrollBy(getScale(it.toDouble())+(scale*1.715).toInt(),0)
                                    }

                                }
                                // Delay the scrollTo to make it work
                            }, 1000);
                        }

                    };


                    emptyViewPerson.showContent();
                    }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
                Log.d("======== >ScaleChanged",oldScale.toString()+ " " + newScale.toString());
            }
        })
    }
    private fun  calculateProgression(myWebView: WebView) :Int
    {
        var positionTopView  = myWebView.getTop();
        var contentHeight = myWebView.getContentHeight();
        var currentScrollPosition = myWebView.getScrollY();
        var percentWebview = (currentScrollPosition - positionTopView) / contentHeight;
        return percentWebview;
    }
    private fun getScale(PIC_WIDTH:Double): Int {

        val display =
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = display.width
        var `val`: Double = width / PIC_WIDTH
        `val` = `val` * 100
        return `val`.toInt()
    }

    class ServicesViewModel : ViewModel() {
        val services = MutableLiveData<List<HealthCareService>>()
        val loading = MutableLiveData<Boolean>()
        val exception = MutableLiveData<Throwable>()
    }
    class WebAppInterface(private val mContext: Context,private val root_layout: LinearLayout, private val org: Organization,private val intent: Intent) {


        private lateinit var viewModel: ServicesViewModel
//        private var _intent:Intent = intent
        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
            var builder:AlertDialog.Builder = AlertDialog.Builder(mContext);
            builder.setCancelable(false);
            builder.setMessage(toast);
            builder.setPositiveButton(R.string.ok, DialogInterface.OnClickListener() {
                dialogInterface: DialogInterface, i: Int ->

            });
            var dialog = builder.create();
            dialog.show();

        }
        @JavascriptInterface
        fun showMessage(){
            Thread(Runnable {
                // try to touch View of UI thread
                 mContext.runOnUiThread {
                     var tverrorMessage = root_layout.findViewById<TextView>(R.id.errorMessage);
                     tverrorMessage.visibility = View.VISIBLE
                 }
            }).start()

        }
        @JavascriptInterface
        fun showPersonalInfo(id:String,personId:String) {
            var user = auth(mContext).user
            if (user!!.roles[0] != User.Role.SURVEYOR ) {
                intent.putExtra("id", id)
                intent.putExtra("personId", personId)
                mContext.startActivity(intent)
            }
//            val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)  as LayoutInflater
//            val view = inflater.inflate(layout.popup_person_info,null)
//
//            val popupWindow = PopupWindow(
//                view,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//
//            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
//                popupWindow.elevation = 10.0F
//            }
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            {
//                val slideIn = Slide()
//                slideIn.slideEdge = Gravity.TOP
//                popupWindow.enterTransition = slideIn
//
//                val slideOut = Slide()
//                slideOut.slideEdge = Gravity.RIGHT
//                popupWindow.exitTransition = slideOut
//            }
//
//            val buttonPopup = view.findViewById<Button>(R.id.button_popup)
//                buttonPopup.text = "Close"
//                buttonPopup.setOnClickListener{
//                popupWindow.dismiss()
//            }
//
//            loadHealthcareServices(personId,org,view);
//            TransitionManager.beginDelayedTransition(root_layout)
//            popupWindow.showAtLocation(
//                root_layout,
//                Gravity.CENTER,
//                0,
//                0
//            )
//

        }

//        private fun loadHealthcareServices(personId: String,org: Organization,view :View) {
//            viewModel.loading.value = true
//            var data: List<HealthCareService> = emptyList()
//
//            healthCareServicesOf(personId!!, org!!.id).all {
//                    always { viewModel.loading.value = false }
//                    onFound { viewModel.services.value = it.sortedByDescending { it.time } }
//                    onNotFound { viewModel.services.value = emptyList() }
//                    onFail { viewModel.exception.value = it }
//                }
//
//                Bind(data,view)
//
//
//        }
        private fun Bind(data:List<HealthCareService>,view :View){
            if(data!=null){
                if(data.size>0) {
                    if (data[0].bmi!!.value < 18.5) {
                        val tr = view.findViewById<TableRow>(R.id.bmi_revel1)
                        if (tr != null) {
                            tr.setBackgroundColor(Color.parseColor("#e74c3c"))
                        }
                    }
                    if (data[0].bmi!!.value >= 18.5 && data[0].bmi!!.value <= 22.90) {
                        val tr = view.findViewById<TableRow>(R.id.bmi_revel2)
                        if (tr != null) {

                            tr.setBackgroundColor(Color.parseColor("#e67e22"))
                        }
                    }
                    if (data[0].bmi!!.value >= 23 && data[0].bmi!!.value <= 24.90) {
                        val tr = view.findViewById<TableRow>(R.id.bmi_revel3)
                        if (tr != null) {
                            tr.setBackgroundColor(Color.parseColor("#2ecc71"))
                        }
                    }
                    if (data[0].bmi!!.value >= 25 && data[0].bmi!!.value <= 29.90) {
                        val tr = view.findViewById<TableRow>(R.id.bmi_revel4)
                        if (tr != null) {
                            tr.setBackgroundColor(Color.parseColor("#f1c40f"))
                        }
                    }
                    if (data[0].bmi!!.value >= 30) {
                        val tr = view.findViewById<TableRow>(R.id.bmi_revel5)
                        if (tr != null) {
                            tr.setBackgroundColor(Color.parseColor("#f49c13"))
                        }
                    }
                }
            }
        }

    }
}
