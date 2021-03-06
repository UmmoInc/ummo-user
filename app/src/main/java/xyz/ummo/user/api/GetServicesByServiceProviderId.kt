package xyz.ummo.user.api

import android.app.Activity
import com.github.kittinunf.fuel.Fuel

abstract class GetServicesByServiceProviderId(activity: Activity, serviceProviderId: String) {
    init {
        Fuel.get("/product?service_provider=$serviceProviderId")
                .response { request, response, result ->
                    activity.runOnUiThread {
                        done(response.data, response.statusCode)
//                        Timber.e("RESPONSE DATA -> ${String(response.data)}")
                    }
                }
    }

    abstract fun done(data: ByteArray, code: Number)
}