package ie.wit.hive.views.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.wit.hive.databinding.ActivityLoginBinding
import ie.wit.hive.databinding.ActivityRegisterBinding

class RegisterView : AppCompatActivity() {
    lateinit var presenter: RegisterPresenter
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        presenter = RegisterPresenter(this)
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.signUp.setOnClickListener {
            val userName = binding.registerUsername.text.toString()
            val firstName = binding.registerFirstname.text.toString()
            val secondName = binding.registerSecondname.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            if (userName == "" || firstName == "" || secondName == "") {
                showSnackBar("please provide all details")
            } else {
                presenter.doSignUp(email, password)
            }
        }

    }

    fun showSnackBar(message: CharSequence) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .show()
    }

    fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}