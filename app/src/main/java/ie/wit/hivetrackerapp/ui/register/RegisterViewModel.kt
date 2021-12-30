package ie.wit.hivetrackerapp.ui.register

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.activities.Home
import ie.wit.hivetrackerapp.main.MainApp
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.UserManager
import ie.wit.hivetrackerapp.models.UserModel


class RegisterViewModel : ViewModel() {

    lateinit var app: Home
    app = activity as Home



    fun getTag(): Long {
        val tag = HiveManager.getTag()
        return  tag
    }

    fun create(user: UserModel) {

        users.create(user)

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