package ie.wit.hive.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hive.R
import ie.wit.hive.main.MainApp
import ie.wit.hive.models.HiveFireStore
import ie.wit.hive.models.HiveModel
import ie.wit.hive.models.UserFireStore
import ie.wit.hive.models.UserModel
import ie.wit.hive.views.hivelist.HiveListView
import kotlinx.coroutines.runBlocking


class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: HiveFireStore? = null
    var userFireStore: UserFireStore? = null


    init{
        registerLoginCallback()
        if (app.hives is HiveFireStore) {
            fireStore = app.hives as HiveFireStore
        }
        if (app.users is UserFireStore) {
            userFireStore = app.users as UserFireStore
        }
    }


    suspend fun checkUserDetails():Boolean{
        runBlocking { userFireStore!!.fetchUsers(){  } }
        val users = app.users.findAll()
        var foundUser = users.find { p -> p.fbId == "11"}
        return foundUser != null
    }

    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchHives {
                        view?.hideProgress()
                        val launcherIntent = Intent(view, HiveListView::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                } else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, HiveListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view?.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()

        }

    }

    fun doSignUp() {
        val launcherIntent = Intent(view, RegisterView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}