package xyz.ummo.bite.localdatabase.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.ummo.bite.localdatabase.maindatabase.Database
import xyz.ummo.bite.localdatabase.models.Profile
import xyz.ummo.bite.localdatabase.repositories.profileRepository

class profileViewModel(
    application: Application

): AndroidViewModel(application) {
    val TopMostUser:MutableLiveData<Profile> =MutableLiveData()

    private val repository: profileRepository


    init {
        val userDao = Database.getDatabase(application)?.getProfileDao()
        repository = userDao?.let { profileRepository(it) }!!
        viewModelScope.launch {
               Timber.e("dbInitialize->databasecreated")
            Log.d("db_initialize","databaseCreated")


        }
    }

    fun addProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProfile(profile)
        }
    }

    fun getTopUser(){
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.getTopUser().let {
               TopMostUser.postValue(it)
                Timber.e(
                    "TopUser->${
                        TopMostUser.value
                            ?.email
                    }"
                )
                Log.d("TopUser","  ${TopMostUser.value?.email}")
                 Log.d("TopUserDirect","$it")
            }
        }
    }


  }