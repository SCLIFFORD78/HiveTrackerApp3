package ie.wit.hive.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import ie.wit.hive.main.MainApp
import ie.wit.hive.models.HiveFireStore
import ie.wit.hive.views.hivelist.HiveListView



class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: HiveFireStore? = null

    init{
        registerLoginCallback()
        if (app.hives is HiveFireStore) {
            fireStore = app.hives as HiveFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            var test = task.isSuccessful
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

    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchHives {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, HiveListView::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}