package ie.wit.hivetrackerapp.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hivetrackerapp.models.*
import timber.log.Timber
import timber.log.Timber.i


class MainApp : Application() {

    //var hives: HiveStore = HiveJSONStore(applicationContext)
    lateinit var hives: List<HiveModel>
    //var users: UserStore = UserJSONStore(applicationContext)
    lateinit var users: List<UserModel>


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        hives = HiveManager.findAll()
        users = UserManager.findAll()
        var defaultUser =   UserModel()
        defaultUser.firstName = "John"
        defaultUser.lastName = "Clifford"
        defaultUser.email = "johnc@gmail.com"
        UserManager.create(defaultUser)



        //hives = HiveMemStore()
        i("HiveTracker started")
        //hives.add(HiveModel("One", "About one..."))
        //hives.add(HiveModel("Two", "About two..."))
        //hives.add(HiveModel("Three", "About three..."))
    }
}
