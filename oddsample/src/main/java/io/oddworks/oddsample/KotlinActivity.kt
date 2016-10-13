package io.oddworks.oddsample

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import io.oddworks.device.model.OddConfig
import io.oddworks.device.model.common.OddResourceType
import io.oddworks.device.request.OddRequest
import io.oddworks.device.request.RxOddCall

class KotlinActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initializeOddData()
    }

    private fun initializeOddData() {
        RxOddCall.observableFrom<OddConfig> {
            OddRequest.Builder(this).resourceType(OddResourceType.CONFIG).build().enqueueGetRequest(it)
        }
    }
}
