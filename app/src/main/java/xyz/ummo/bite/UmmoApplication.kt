package xyz.ummo.bite

import android.app.Application
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import xyz.ummo.bite.BuildConfig
import xyz.ummo.bite.sharedpreferences.Prefs



class UmmoApplication : Application() {

private  lateinit var prefs:Prefs

companion object{

    var prefs: Prefs? = null
    lateinit var instance: UmmoApplication
        private set


}

    override fun onCreate() {
        super.onCreate()

        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
           // .addIndex(MyEventBusIndex())
            .installDefaultEventBus()
        prefs = Prefs(applicationContext)





    }


    init {

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

        }

    }

 }