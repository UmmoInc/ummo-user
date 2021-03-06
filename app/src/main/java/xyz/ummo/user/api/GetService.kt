package xyz.ummo.user.api

import android.content.Context
import android.preference.PreferenceManager
import com.github.kittinunf.fuel.Fuel

abstract class GetService(context: Context, service_id: String) {
    init {
        val jwt = PreferenceManager.getDefaultSharedPreferences(context).getString("jwt", "jwt")
        Fuel.get("/service/$service_id")
                .response { request, response, result ->
                    done(response.data,response.statusCode)
                }
    }

    abstract fun done(data:ByteArray,code:Number)
}