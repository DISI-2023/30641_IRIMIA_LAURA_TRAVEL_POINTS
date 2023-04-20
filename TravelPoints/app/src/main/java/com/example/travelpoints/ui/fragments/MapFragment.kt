package com.example.travelpoints.ui.fragments

import android.view.LayoutInflater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.example.travelpoints.databinding.FragmentMapBinding
import com.example.travelpoints.helpers.LocationPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MapFragment(private val navigateToSiteCreation: (Double, Double) -> Unit) : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var locationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        locationRequest = LocationRequest.create()
        LocationPermission().instanceLocationRequest(locationRequest)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)



        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        LocationPermission().getCurrentLocation(requireActivity(), locationRequest, map)

        map.setOnMapLongClickListener {
            //if (FirebaseAuth.getInstance().currentUser?.uid.equals("LWtquFDJ0MWlXqTc6ZsukLAklvb2")) {
//                val databaseReference = FirebaseDatabase.getInstance().getReference("Site").child("Location")
//                databaseReference.child("Latitude").setValue(it.latitude)
//                databaseReference.child("Longitude").setValue(it.longitude)
                navigateToSiteCreation(it.latitude, it.longitude)

            }
        //}
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}