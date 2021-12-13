package ie.wit.hivetrackerapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import timber.log.Timber
import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.adapters.HiveTrackerAdapter
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.ui.auth.LoggedInViewModel
import ie.wit.hivetrackerapp.ui.update.UpdateFragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import ie.wit.hivetrackerapp.databinding.HomeBinding
import ie.wit.hivetrackerapp.databinding.NavHeaderBinding
import ie.wit.hivetrackerapp.ui.auth.Login

class Home : AppCompatActivity(), HiveTrackerAdapter.Communicator {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private val mFragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.addFragment,
            R.id.listFragment,
            R.id.aboutusFragment,
            //R.id.updateFragment,
            R.id.accountDetailsFragment,
            R.id.loginOrRegisterFragment
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderEmail.text = currentUser.email
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    @SuppressLint("WrongConstant")
    override fun passDataCom(hive: HiveModel) {
        val mFragment = UpdateFragment()
        Timber.i("testing data sent to home page : $hive")
        val mBundle = Bundle()
        mBundle.putParcelable("data",hive)
        mFragment.arguments=mBundle
        mFragment.data = hive
        Timber.i("testing data sent to home page : $mBundle")
        mFragmentManager.beginTransaction().replace(R.id.update,mFragment).addToBackStack(null).commit()

    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



}