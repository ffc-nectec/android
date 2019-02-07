package ffc.app

import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ffc.android.browsePlayStore
import ffc.android.observe
import ffc.android.viewModel
import ffc.api.FfcCentral
import ffc.app.auth.LoginActivity
import ffc.app.auth.auth
import ffc.app.auth.legal.LegalActivity
import ffc.app.util.SimpleViewModel
import ffc.app.util.version.Version
import ffc.app.util.version.versionCheck
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_login.ivCommunity
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import timber.log.Timber

class SplashScreenActivity : FamilyFolderActivity() {

    val viewModel by lazy { viewModel<SimpleViewModel<Version>>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        blurBackgroundImage()

        Handler().postDelayed({
            versionCheck().checkForUpdate {
                onFound { viewModel.content.value = it }
                onNotFound { viewModel.content.value = null }
                onFail {
                    viewModel.content.value = null
                    viewModel.exception.value = it
                }
            }
        }, 1500)

        observe(viewModel.content) {
            if (it != null) {
                //Show update dialog
                alert("พบเวอร์ชั่นใหม่ (v$it) กรุณาทำการอัพเดทก่อนเข้าใช้งาน", getString(R.string.update)) {
                    isCancelable = false
                    positiveButton(R.string.update) {
                        browsePlayStore(BuildConfig.APPLICATION_ID)
                        finishAffinity()
                    }
                    negativeButton(R.string.close_app) { finishAffinity() }
                }.show()
            } else {
                //Already up to date
                gotoNextActivity()
            }
        }
        observe(viewModel.exception) {
            Timber.i(it, "Can't check latest release version")
        }
    }

    private fun gotoNextActivity() {
        val auth = auth(this)
        if (auth.isLoggedIn) {
            FfcCentral.token = auth.token
            startActivity<MainActivity>()
            overridePendingTransition(0, android.R.anim.slide_out_right)
            startActivity<LegalActivity>()
        } else {
            startActivity<LoginActivity>()
            overridePendingTransition(0, android.R.anim.fade_out)
        }
        finish()
    }

    private fun blurBackgroundImage() {
        Glide.with(this)
            .load(R.drawable.community)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(4, 3)))
            .into(ivCommunity)
    }
}
