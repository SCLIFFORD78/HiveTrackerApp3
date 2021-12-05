package ie.wit.hivetrackerapp.ui.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.hivetrackerapp.main.MainApp
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.UserManager
import ie.wit.hivetrackerapp.models.UserModel
import timber.log.Timber
import java.lang.Exception

class UpdateViewModel : ViewModel() {

    private val hivesList =
        MutableLiveData<List<HiveModel>>()

    private val usersList = MutableLiveData<List<UserModel>>()
    val observableHivesList: LiveData<List<HiveModel>>
        get() = hivesList
    val observableUsersList: LiveData<List<UserModel>>
        get() = usersList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            hivesList.value = HiveManager.findAll()
            Timber.i("Report Load Success : ${hivesList.value.toString()}")
            usersList.value = UserManager.findAll()
            Timber.i("Report Load Success : ${usersList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun update(hive: HiveModel) {
        HiveManager.update(hive)
    }

    fun delete(hive: HiveModel) {
        HiveManager.delete(hive)
    }
}