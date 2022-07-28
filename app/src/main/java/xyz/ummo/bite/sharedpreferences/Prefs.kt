package xyz.ummo.bite.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    //Declare a private constant variable that can access the shared preferences file




    private val APP_PREF_1 = "xyz.ummo.bite.userdatapreference"
     val preferences: SharedPreferences = context.getSharedPreferences(APP_PREF_1,Context.MODE_PRIVATE)
    fun editor() = preferences.edit()


}