/*
 * Copyright (c) 2019 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ffc.app.auth.legal

//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ffc.android.observe
import ffc.android.viewModel
import ffc.api.ApiErrorException
import ffc.api.FfcCentral
import ffc.app.FamilyFolderActivity
import ffc.app.MainActivity
import ffc.app.R
import ffc.app.util.alert.handle
import org.jetbrains.anko.startActivity
import retrofit2.dsl.enqueue

class LegalAgreementActivity : FamilyFolderActivity() {

    val api = FfcCentral().service<LegalAgreementApi>()
    val viewModel by lazy { viewModel<AgreementViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.legal_activity)

        observe(viewModel.activeType) {
            if (it == null) {
                startActivity<MainActivity>()
                overridePendingTransition(0, R.anim.design_bottom_sheet_slide_out)
                finish()
            } else {
                val fragment = LegalAgreementFragment().apply {
                    legalDocCall = api.latest(it)
                    onAccept = { version ->
                        api.agreeWith(it, version, currentUser!!.id, currentUser!!.orgId!!).enqueue {
                            onSuccess {
                                val queue = viewModel.queueType.value
                                queue?.remove(it)
                                viewModel.activeType.value = queue?.getOrNull(0)
                            }
                            onError { viewModel.exception.value = ApiErrorException(this) }
                            onFailure { viewModel.exception.value = it }
                        }
                    }
                }
                supportFragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit()
            }
        }
        observe(viewModel.exception) {
            it?.let { handle(it) }
        }
        viewModel.queueType.value = mutableListOf()

        if (currentUser != null) {
            checkAgreement(LegalType.privacy)
            checkAgreement(LegalType.terms)
        }
    }

    private val requireDoc = 2
    private var responseDoc = 0
        set(value) {
            if (value == requireDoc) {
                viewModel.activeType.value = viewModel.queueType.value?.getOrNull(0)
            }
            field = value
        }

    private fun checkAgreement(type: LegalType) {
        api.checkAgreement(type, currentUser!!.id, currentUser!!.orgId!!).enqueue {
            onSuccess { /* Do nothing */ }
            onError {
                when (code()) {
                    404 -> viewModel.queueType.value!!.add(type)
                    else -> viewModel.exception.value = ApiErrorException(this)
                }
            }
            onFailure { viewModel.exception.value = it }
            finally { responseDoc++ }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.design_bottom_sheet_slide_out)
    }

    class AgreementViewModel : ViewModel() {
        val queueType: MutableLiveData<MutableList<LegalType>> = MutableLiveData()
        val activeType: MutableLiveData<LegalType> = MutableLiveData()
        val exception: MutableLiveData<Throwable> = MutableLiveData()
    }
}
