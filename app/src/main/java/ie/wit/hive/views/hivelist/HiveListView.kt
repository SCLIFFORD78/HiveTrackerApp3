package ie.wit.hive.views.hivelist
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
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
        //if (user != null) {
        //    binding.toolbar.title = "${title}: ${user.email}"
        //}
        setSupportActionBar(binding.toolbar)

        presenter = HiveListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        updateRecyclerView(0)
    }


    override fun onResume() {

        //update the view
        super.onResume()
        updateRecyclerView(0)
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHive() }
            R.id.item_map -> { presenter.doShowHivesMap() }
            R.id.aboutus -> { presenter.doShowAboutUs() }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.queryHint = "Tag Number or 0 for all hives"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var value = ""
                var num:Long = 0
                if (query == ""){
                    value = num.toString()
                }else{
                    if (query != null) {
                        value = query
                    }
                }
                if(value.toString().toLong() > 0 && value !=null){
                    updateRecyclerView(query.toString().toLong())
                    searchView.clearFocus()
                }else if(value.toString().toInt() == 0 ){
                    updateRecyclerView(0)
                    searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var value = ""
                var num:Long = 0
                if (newText == ""){
                    value = num.toString()
                }else{
                    if (newText != null) {
                        value = newText
                    }
                }
                if(value.toLong() > 0 && value !=null){
                    updateRecyclerView(newText.toString().toLong())
                    searchView.clearFocus()
                }else if(value.toInt() == 0 ){
                    updateRecyclerView(0)
                    searchView.clearFocus()
                }



                return false
            }


        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun updateRecyclerView(tag:Long){
        var test = tag
        if (tag > 0 && tag != null){
            GlobalScope.launch(Dispatchers.Main) {
                binding.recyclerView.adapter =
                    HiveAdapter(presenter.getHiveByTag(tag), this@HiveListView)
            }
        }else{
            GlobalScope.launch(Dispatchers.Main){
            binding.recyclerView.adapter =
                HiveAdapter(presenter.getHives(), this@HiveListView)
        }


        } }

}