package ie.wit.hivetrackerapp.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hivetrackerapp.models.*
import ie.wit.hivetrackerapp.room.UserStoreRoom
import ie.wit.usertrackerapp.models.UserJSONStore
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    //var hives: HiveStore = HiveJSONStore(applicationContext)
    lateinit var hives: HiveStore
    //var users: UserStore = UserJSONStore(applicationContext)
    lateinit var users: UserStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hives = HiveJSONStore(applicationContext)
        users = UserStoreRoom(applicationContext)
    }
}
