package xyz.ummo.user.delegate

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import timber.log.Timber
import xyz.ummo.user.R

/** This abstract function takes a User (contact) && Product (ID); then a request
 *  is made with Fuel (HTTP POST) **/
abstract class RequestService(context: Context?, user: String, product: String) {

    private lateinit var alertDialog: MaterialAlertDialogBuilder

    init {
        val alertDialogBuilder = MaterialAlertDialogBuilder(context!!)

        val completingAlertDialogView = LayoutInflater.from(context)
                .inflate(R.layout.completing_request_dialog, null)

        val errorAlertDialogView = LayoutInflater.from(context)
                .inflate(R.layout.request_error_dialog, null)

        val data = JSONObject()
        data.put("user_id", user)
        data.put("product_id", product)

        Fuel.post("${context.getString(R.string.serverUrl)}/api/service_request")
                .jsonBody(data.toString())
                .response { request, response, result ->
                    (context as Activity).runOnUiThread {
                        done(response.data, response.statusCode)

                        Timber.e("Request Body->${request.body}")
                        Timber.e("Response Code->${response.statusCode}")
                        Timber.e("Result Body->${result}")

                        if (response.statusCode == 200) {
                            alertDialogBuilder.setTitle("Making Request")
                                    .setIcon(R.drawable.logo)
                                    .setView(completingAlertDialogView)
                                    .setPositiveButton("Got It") { dialogInterface, i ->
                                        val bottomNavigationView = context.findViewById<View>(R.id.bottom_nav)
                                        val snackbar = Snackbar.make(context.findViewById(android.R.id.content), "Your request was successful!", Snackbar.LENGTH_LONG)
                                        snackbar.setTextColor(context.resources.getColor(R.color.ummo_4))
                                        snackbar.anchorView = bottomNavigationView
                                        snackbar.show()
                                    }

                            alertDialogBuilder.show()

                            Thread {
                                try {
                                    Thread.sleep(1000)

                                } catch (ie: InterruptedException) {
                                    Timber.e("Thread exception -> $ie")
                                }
                            }.start()

                        } else if (response.statusCode == 500) {
                            alertDialogBuilder.setTitle("Something's Wrong")
                                    .setIcon(R.drawable.logo)
                                    .setView(errorAlertDialogView)

                            alertDialogBuilder.setPositiveButton("Feedback") { dialogInterface, i ->
                                //TODO: add feedback functionality
                                /*val feedbackFromMainScreen = MainScreen()
                                feedbackFromMainScreen.feedback()*/
                            }

                            alertDialogBuilder.setNegativeButton("Later") { dialogInterface, i ->
                                //TODO: track `Later` event
                            }

                            alertDialogBuilder.show()

                            Thread {
                                try {
                                    Thread.sleep(5000)

                                } catch (ie: InterruptedException) {
                                    Timber.e("Thread exception -> $ie")
                                }
                            }
                        }
                    }
                }
    }

    abstract fun done(data: ByteArray, code: Int)
}