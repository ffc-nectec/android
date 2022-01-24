/*
 * Copyright (c) 2018 NECTEC
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

package ffc.app.health

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.berry_med.monitordemo.activity.DeviceMainActivity
import ffc.android.check
import ffc.android.isNotBlank
import ffc.android.notEmpty
import ffc.android.onClick
import ffc.app.R
import ffc.app.health.service.HealthCareServivceForm
import ffc.app.util.setInto
import ffc.entity.healthcare.BloodPressure
import ffc.entity.healthcare.HealthCareService
import ffc.entity.update
import kotlinx.android.synthetic.main.hs_vitalsign_form_fragment.*
import org.jetbrains.anko.support.v4.startActivity

internal class VitalSignFormFragment : Fragment(), HealthCareServivceForm<HealthCareService> {

    override fun bind(service: HealthCareService) {
        with(service) {
            bloodPressure?.let {
                it.systolic.setInto(bpSysField)
                it.diastolic.setInto(bpDiaField)
            }
            pulseRate.setInto(pulseField)
            respiratoryRate.setInto(rrField)
            bodyTemperature.setInto(tempField)
        }
    }

    var service: HealthCareService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hs_vitalsign_form_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        service?.let {
            it.bloodPressure?.let { bp ->
                bpSysField.setText("${bp.systolic}")
                bpDiaField.setText("${bp.diastolic}")
            }
            it.respiratoryRate?.let { rr -> rrField.setText("$rr") }
            it.pulseRate?.let { pr -> pulseField.setText("$pr") }
            it.bodyTemperature?.let { temp -> tempField.setText("$temp") }
        }
        btnMeasuringTools.onClick {
            var intent = Intent(context,DeviceMainActivity::class.java)
            startActivityForResult(intent,1)
        }
        btnPressure.onClick{
            var intent = Intent(context,samplecode_north_vision_lifecare_10.LifeCareActivity::class.java)
            startActivityForResult(intent,2)
        }
        btnTemperator.onClick{
            var intent = Intent(context,samplecode_north_vision_thermometer.Activity.ThermometerActivity::class.java)
            startActivityForResult(intent,3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == 1) {
                var ecgInfo = data!!.getStringExtra("ECGInfo");
                var spO2Info = data!!.getStringExtra("SPO2Info");

                var tempInfo = data!!.getStringExtra("TEMPInfo");
                var nibpInfo = data!!.getStringExtra("NIBPInfo");

                var ecgTemp = ecgInfo?.split(":");
                var heartRate = ecgTemp?.get(1)?.replace("Resp Rate","");
                var RespRate = ecgTemp?.get(2);

                var spO2Temp = spO2Info?.split(":")
                var spO2 = spO2Temp?.get(1)?.replace("SPO2","")
                var spO2PluseRate = spO2Temp?.get(2)

                var strHigh = "High:";
                var strLow ="Low:"
                var strMean ="Mean:"
                var indexHigh = nibpInfo?.indexOf(strHigh);
                var indexLow = nibpInfo?.indexOf(strLow);
                var indexMean = nibpInfo?.indexOf(strMean);
                var hight= indexHigh?.plus(strHigh.length)?.let { indexLow?.minus(1)?.let { it1 ->
                    nibpInfo?.substring(it,
                        it1
                    )
                } };
                var low= indexLow?.plus(strLow.length)?.let { indexMean?.minus(1)?.let { it1 ->
                    nibpInfo?.substring(it,
                        it1
                    )
                } };
                var tmp = tempInfo?.replace("TEMP:","")!!.replace("°C","").trim();
                if(tmp.trim().indexOf("-")<0) {
                    tempField.setText(tmp);
                }
                if(hight!!.indexOf("-")<0) {
                    bpSysField.setText(hight)
                }
                if(low!!.indexOf("-")<0) {
                    bpDiaField.setText(low)
                }
                if(spO2PluseRate!!.indexOf("-")<0) {

                    pulseField.setText(spO2PluseRate)
                }
                if(RespRate!!.indexOf("-")<0) {
                    rrField.setText(RespRate)
                }
            }
            else if(requestCode==2){
                var sysInfo = data!!.getStringExtra("SYSInfo");
                bpSysField.setText(sysInfo)

                var diaInfo = data!!.getStringExtra("DIAInfo");
                bpDiaField.setText(sysInfo)
            }
            else if(requestCode==3){
                var tempInfo = data!!.getStringExtra("TEMPInfo");
                tempField.setText(tempInfo);
            }
        }
    }

    override fun dataInto(service: HealthCareService) {
        bpSysField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 80.0..330.0 }
            message = "ค่าต้องอยู่ระหว่าง 80-330"
        }
        bpSysField.check {
            on { bpDiaField.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        bpDiaField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 30.0..135.0 }
            message = "ค่าต้องอยู่ระหว่าง 30-135"
        }
        bpDiaField.check {
            on { bpSysField.isNotBlank }
            that { isNotBlank }
            message = "กรุณาระบุ"
        }
        pulseField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 30.0..250.0 }
            message = "ค่าต้องอยู่ระหว่าง 30-250"
        }
        rrField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 5.0..40.0 }
            message = "ค่าต้องอยู่ระหว่าง 5-40"
        }
        tempField.check {
            on { isNotBlank }
            that { text.toString().toDouble() in 35.0..45.0 }
            message = "ค่าต้องอยู่ระหว่าง 35-45"
        }

        service.update {
            if (notEmpty(bpSysField, bpDiaField))
                bloodPressure = BloodPressure(
                    bpSysField.text.toString().toDouble(),
                    bpDiaField.text.toString().toDouble())
            if (notEmpty(rrField))
                respiratoryRate = rrField.text.toString().toDouble()
            if (notEmpty(pulseField))
                pulseRate = pulseField.text.toString().toDouble()
            if (notEmpty(tempField))
                bodyTemperature = tempField.text.toString().toDouble()
        }
    }
}
