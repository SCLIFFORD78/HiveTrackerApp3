package ie.wit.hive.views.hivelist
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import ie.wit.hive.R
import ie.wit.hive.adapters.HiveAdapter
import ie.wit.hive.adapters.HiveListener
import ie.wit.hive.databinding.ActivityHiveListBinding
import ie.wit.hive.main.MainApp
import ie.wit.hive.models.HiveModel
import timber.log.Timber.i

class HiveListView : AppCompatActivity(), HiveListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityHiveListBinding
    lateinit var presenter: HiveListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityHiveListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //update Toolbar title
        binding.toolbar.title = title
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }
        setSupportActionBar(binding.toolbar)

        presenter = HiveListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {

        //update the view
        super.onResume()
        updateRecyclerView()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHive() }
            R.id.item_map -> { presenter.doShowHivesMap() }
            R.id.item_logout -> {
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doLogout()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHiveClick(hive: HiveModel) {
        presenter.doEditHive(hive)

    }

    private fun updateRecyclerView(){
        GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                HiveAdapter(presenter.getHives(), this@HiveListView)
        }
    }

}