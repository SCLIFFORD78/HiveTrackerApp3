package ie.wit.hivetrackerapp.ui.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast

import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso

import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.activities.MapsActivity
import ie.wit.hivetrackerapp.databinding.FragmentAddBinding
import ie.wit.hivetrackerapp.helpers.showImagePicker
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.Location
import ie.wit.hivetrackerapp.ui.list.ListViewModel
import timber.log.Timber

var hive = HiveModel()

class AddFragment : Fragment() {

    private var _fragBinding: FragmentAddBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var location = Location()
    private lateinit var data: HiveModel
    private lateinit var spinner: Spinner
    private val addViewModel: AddViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = HiveModel()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        hive=HiveModel()
        _fragBinding = FragmentAddBinding.inflate(inflater, container, false)
        val root = fragBinding.root


        spinner = root.findViewById(R.id.hiveTypeSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hiveType, android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            adapter.also { spinner.adapter = it }
        }
        fragBinding.hiveTitle.setText(addViewModel.getTag().toString())

        setAddButtonListener(fragBinding)
        setChooseImageListener(fragBinding)
        registerImagePickerCallback(fragBinding)
        setChooseMapListener(fragBinding)
        registerMapCallback()
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
        return super.onOptionsItemSelected(item)
    }

    private fun setAddButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            hive.tag = layout.hiveTitle.text.toString().toLong()
            hive.description = layout.description.text.toString()
            hive.type = spinner.selectedItem.toString()
            hive.userID = addViewModel.loggedInUser().id
            addViewModel.create(hive.copy())
            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
            Timber.i("Add Hive Button Pressed: ${addViewModel.loggedInUser().userName}")
        }
    }

    private fun setChooseImageListener(layout: FragmentAddBinding){
        layout.chooseImage.setOnClickListener{
            showImagePicker(imageIntentLauncher)
        }
    }

    private fun setChooseMapListener(layout: FragmentAddBinding){
        layout.hiveLocation.setOnClickListener{
            Timber.i("Set Location Pressed")
            val location = Location(52.0634310, -9.6853542, 15f)
            if (hive.zoom != 0f) {
                location.lat =  hive.lat
                location.lng = hive.lng
                location.zoom = hive.zoom
            }
            val launcherIntent = Intent(activity, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerImagePickerCallback(layout: FragmentAddBinding) {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    -1 -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            hive.image = result.data!!.data!!
                            Picasso.get()
                                .load(hive.image)
                                .into(layout.hiveImage)
                            result.data!!.data = null
                        } // end of if
                    }
                    0 -> {
                    }
                    else -> {
                        Timber.i("Image selection cancelled")
                    }
                }
            }

    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                Timber.i("Map data ${result.data.toString()}")
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            hive.lat = location.lat
                            hive.lng = location.lng
                            hive.zoom = location.zoom
                            Timber.i("Location == $location")
                        } // end of if
                    }
                    Activity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}