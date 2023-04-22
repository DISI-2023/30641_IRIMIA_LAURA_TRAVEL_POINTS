package com.example.travelpoints.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelpoints.adapters.SearchViewAdapter
import com.example.travelpoints.databinding.FragmentMapBinding
import com.example.travelpoints.helpers.LocationPermission
import com.example.travelpoints.ui.viewmodels.MapFragmentViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapFragment(private val navigateToSiteCreation: (Double, Double) -> Unit) : Fragment(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var locationRequest: LocationRequest
    private val viewModel by lazy {
        ViewModelProvider(this)[MapFragmentViewModel::class.java]
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllSites()

        val adapter = SearchViewAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter.onSiteClicked = ::onSiteClicked

        viewModel.sites.observe(viewLifecycleOwner) { sites ->
            if (sites != null) {
                adapter.dataList = sites
            } else {
                adapter.dataList = emptyList()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle search query submission
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                newText.let { text ->
                    if (text.isNotEmpty() && text.isNotBlank()) {
                        binding.recyclerView.isVisible = true
                        binding.constraintLayout.isVisible = false
                        val newList = mutableListOf<Pair<String, LatLng>>()
                        viewModel.sites.value?.forEach {
                            if (it.first.contains(newText)) {
                                newList.add(it)
                            }
                        }
                        adapter.dataList = newList

                    } else {
                        binding.recyclerView.isVisible = false
                        binding.constraintLayout.isVisible = true
                        
                    }
                }
                return false
            }
        })
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

    private fun onSiteClicked(latLng: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        map.animateCamera(CameraUpdateFactory.zoomIn())
        map.animateCamera(CameraUpdateFactory.zoomTo(13f))
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