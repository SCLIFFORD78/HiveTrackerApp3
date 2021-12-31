package ie.wit.hive.views.hivelist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ie.wit.hive.main.MainApp
import ie.wit.hive.models.HiveModel
import ie.wit.hive.views.aboutus.AboutUsView
import ie.wit.hive.views.login.LoginView
import ie.wit.hive.views.hive.HiveView
import ie.wit.hive.views.map.HiveMapView

class HiveListPresenter(private val view: HiveListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getHives() = app.hives.findAll()

    suspend fun getHiveByTag(tag:Long):List<HiveModel>{
        var list : ArrayList<HiveModel> = arrayListOf()
        var hives = getHives()
        val foundhive = hives.find { p -> p.tag == tag }
        if (foundhive != null) {
            list.add(0,foundhive)
        }

        return list
    }


    fun doAddHive() {
        val launcherIntent = Intent(view, HiveView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditHive(hive: HiveModel) {
        val launcherIntent = Intent(view, HiveView::class.java)
        launcherIntent.putExtra("hive_edit", hive)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowHivesMap() {
        val launcherIntent = Intent(view, HiveMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowAboutUs() {
        val launcherIntent = Intent(view, AboutUsView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.hives.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getHives()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}