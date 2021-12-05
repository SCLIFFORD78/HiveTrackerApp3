package ie.wit.hivetrackerapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.hivetrackerapp.ui.add.hive

import ie.wit.hivetrackerapp.models.UserManager
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.UserModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {


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

    fun findByTag(tag: Long): HiveModel? {
        try {
            val hive =  HiveManager.findByTag(tag)
        }
        catch (e:Exception){
            Timber.i("Failed to get hives")
        }
        return  hive
    }

    fun delete(hive: HiveModel) {
        try {
            HiveManager.delete(hive)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun find(hive: HiveModel): HiveModel? {
        val hive = HiveManager.find(hive)
        return hive
    }

    fun findAll(): MutableList<HiveModel> {
        val hives = HiveManager.findAll()
        return hives.toMutableList()
    }


    fun findByType(type: String): List<HiveModel> {
        val hives = HiveManager.findByType(type)
        return  hives
    }

    fun findByOwner(userID: Long): List<HiveModel> {
        val hives = HiveManager.findByOwner(userID)
        return hives
    }
}