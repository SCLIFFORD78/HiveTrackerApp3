package ie.wit.hivetrackerapp.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hivetrackerapp.R
import ie.wit.hivetrackerapp.databinding.FragmentAddBinding
import ie.wit.hivetrackerapp.databinding.FragmentListBinding
import ie.wit.hivetrackerapp.databinding.FragmentMapsBinding
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.ui.utils.createLoader

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private var _fragBinding: FragmentMapsBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)

        fragBinding.mapView.onCreate(savedInstanceState)
        fragBinding.mapView.getMapAsync{
            map = it
            configureMap()
        }
        val root = fragBinding.root


        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        fragBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        fragBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        fragBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragBinding.mapView.onSaveInstanceState(outState)
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        HiveManager.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.tag.toString()).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val hiveID: TextView = fragBinding.hiveID
        hiveID.text = marker.title
        val currentDescription: TextView = fragBinding.currentDescription
        currentDescription.text = marker.title?.let { HiveManager.findByTag(it.toLong())?.type }

        return false
    }


}