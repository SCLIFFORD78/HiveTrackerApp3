package ie.wit.hivetrackerapp.ui.register

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.activities.Home
import ie.wit.hivetrackerapp.databinding.FragmentRegisterBinding
import ie.wit.hivetrackerapp.main.MainApp
import ie.wit.hivetrackerapp.models.UserManager
import ie.wit.hivetrackerapp.models.UserModel
import ie.wit.hivetrackerapp.models.UserStore
import ie.wit.hivetrackerapp.ui.list.ListViewModel
import ie.wit.usertrackerapp.models.UserJSONStore
import timber.log.Timber

private var user= UserModel()


class RegisterFragment : Fragment() {
    private var _fragBinding: FragmentRegisterBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setRegisterButtonListener(fragBinding)
        return root
    }

    private fun setRegisterButtonListener(layout: FragmentRegisterBinding) {
        layout.btnRegister.setOnClickListener {
            user.userName = layout.registerUsername.text.toString().trim()
            user.firstName = layout.registerFirstname.text.toString().trim()
            user.lastName = layout.registerSecondname.text.toString().trim()
            user.email = registerViewModel.loggedInUser()!!.email.toString()
            if (!isUserNameValid(user.userName)) {
                showLoginFailed(R.string.invalid_username)
            }else if (UserManager.findByUsername(user.userName) != null ) {
                showLoginFailed(R.string.invalid_username_notUnique)}
            else if (!isFirstNameValid(user.firstName)) {
                showLoginFailed(R.string.invalid_firstname)
            }else if (!isSecondNameValid(user.lastName)) {
                showLoginFailed(R.string.invalid_lastName)
            }else{
            registerViewModel.create(user)
                Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)}


            Timber.i("add Button Pressed: ${user.userName}")
            //setResult(RESULT_OK)

        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
    private fun isUserNameValid(username: String): Boolean {
        return username.length > 5
    }

    // A placeholder firstname validation check
    private fun isFirstNameValid(firstName: String): Boolean {
        return firstName.length > 2
    }

    // A placeholder secondname validation check
    private fun isSecondNameValid(secondName: String): Boolean {
        return secondName.length > 2
    }



}