package ie.wit.hivetrackerapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.hivetrackerapp.main.MainApp
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.UserModel

class AddViewModel : ViewModel() {

    var app: MainApp = MainApp()


    fun getTag(): Long {
        val tag = HiveManager.getTag()
        return  tag
    }

    fun create(hive: HiveModel) {
        HiveManager.create(hive)
    }

    fun loggedInUser(): FirebaseUser? {
        var user = FirebaseAuth.getInstance().currentUser
        return user
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text
}