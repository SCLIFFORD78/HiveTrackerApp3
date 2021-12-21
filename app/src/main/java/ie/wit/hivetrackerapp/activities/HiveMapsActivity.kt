package ie.wit.hivetrackerapp.activities

import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ie.wit.hivetrackerapp.R

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hivetrackerapp.databinding.ActivityHiveMapsBinding
import ie.wit.hivetrackerapp.databinding.ContentHiveMapsBinding
import ie.wit.hivetrackerapp.main.MainApp
import ie.wit.hivetrackerapp.models.HiveManager
import ie.wit.hivetrackerapp.models.HiveModel
import ie.wit.hivetrackerapp.models.HiveStore


class HiveMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHiveMapsBinding
    private lateinit var contentBinding: ContentHiveMapsBinding
    lateinit var map: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHiveMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        contentBinding = ContentHiveMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            map = it
            configureMap()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        HiveManager.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.id.toString()).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }
}