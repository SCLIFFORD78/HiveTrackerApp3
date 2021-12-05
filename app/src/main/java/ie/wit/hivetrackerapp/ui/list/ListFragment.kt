package ie.wit.hivetrackerapp.ui.list

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.*
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.databinding.FragmentListBinding
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.UserModel
import ie.wit.hivetrackerapp.ui.utils.createLoader
import ie.wit.hivetrackerapp.ui.utils.hideLoader
import ie.wit.hivetrackerapp.ui.utils.showLoader
import org.wit.hivetrackerapp.adapters.HiveTrackerAdapter

class ListFragment : Fragment(), HiveTrackerAdapter.OnHiveClickListener {
    private var _fragBinding: FragmentListBinding? = null

    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private lateinit var comm: HiveTrackerAdapter.Communicator
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var users : MutableLiveData<List<UserModel>>
    private val listViewModel: ListViewModel by activityViewModels()


    fun searchUpdate(hive: List<HiveModel>){
        fragBinding.recyclerView.adapter = HiveTrackerAdapter(hive,this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())


        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        showLoader(loader,"Downloading Hives")
        listViewModel.observableHivesList .observe(viewLifecycleOwner, Observer {
                hives ->
            hives?.let {
                render(hives as ArrayList<HiveModel>)
                hideLoader(loader)
                //checkSwipeRefresh()
            }
        })

        val names: MutableList<String> = arrayListOf()

        listViewModel.observableUsersList .observe(viewLifecycleOwner, Observer {
                users ->
            users?.let {
                users as ArrayList<UserModel>
                for (user in users){
                    names.add(0,(user.firstName+" "+user.secondName))
                }
                //hideLoader(loader)
                //checkSwipeRefresh()
            }
        })




        names.reverse()
        names.add(0,"All Users")

        spinner = root.findViewById(R.id.hiveTypeSpinnerSearch)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hiveTypeSearch, android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            adapter.also { spinner.adapter = it }
        }
        spinner2 = root.findViewById(R.id.hiveOwnerSpinnerSearch)
        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String>? =
            activity?.let { ArrayAdapter<String>(it.applicationContext, android.R.layout.simple_spinner_item, names) }
        dataAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner2.adapter = dataAdapter

        setUpdateSearchButtonListener(fragBinding)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Tag Number"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val list: MutableList<HiveModel> = mutableListOf()
                val hiveByTag = query?.let { listViewModel.findByTag(it.toLong()) }
                if (hiveByTag != null) {
                    list.add(0,hiveByTag)
                    searchUpdate(list)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    override fun onHiveClick(position: Int) {

        val clickedItem = listViewModel.find(listViewModel.findAll().sortedBy { it.tag }[position])
        Timber.i("Item Pressed: $clickedItem clicked")
        comm = requireActivity() as HiveTrackerAdapter.Communicator

        Navigation.findNavController(this.requireView()).navigate(R.id.updateFragment)
        if (clickedItem != null) {
            comm.passDataCom(clickedItem)
        }
    }

    private fun render(hivesList: List<HiveModel>) {
        fragBinding.recyclerView.adapter = HiveTrackerAdapter(hivesList,this)
        if (hivesList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.hivesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.hivesNotFound.visibility = View.GONE
        }
    }

    private fun setUpdateSearchButtonListener(layout: FragmentListBinding) {
        layout.btnUpdateSearch.setOnClickListener {
            val type = spinner.selectedItem.toString()
            var position = spinner2.selectedItemPosition
            if (position < 0){position = 0}
            val returnedHiveTypes:List<HiveModel> = listViewModel.findByType(type).sortedBy { it.tag }
            val returnedHiveUserID:List<HiveModel>

            if (type != "All Hive Types" ) {
                if (position != 0){
                    returnedHiveUserID =
                        this. users.value?.get(0)
                            ?.let { it1 -> findByOwner(it1.id,returnedHiveTypes).sortedBy { it.tag } }!!
                    fragBinding.recyclerView.adapter = HiveTrackerAdapter(returnedHiveUserID, this)
                }else{
                    fragBinding.recyclerView.adapter = HiveTrackerAdapter(returnedHiveTypes, this)
                }
            }else{
                if (position != 0){
                    returnedHiveUserID =
                        this. users.value?.get(0)
                            ?.let { it1 -> listViewModel.findByOwner(it1.id).sortedBy { it.tag } }!!
                    fragBinding.recyclerView.adapter = HiveTrackerAdapter(returnedHiveUserID, this)
                }else{
                    fragBinding.recyclerView.adapter = HiveTrackerAdapter(listViewModel.findAll().sortedBy { it.tag },this)
                }
            }

        }
    }
    fun findByType(type: String, hives:List<HiveModel>): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.type == type) {
            resp.add(0,hive)
        }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }


    private fun findByOwner(userID: Long, hives:List<HiveModel>): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.userID == userID) {
            resp.add(0,hive)
        }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }

    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }



}