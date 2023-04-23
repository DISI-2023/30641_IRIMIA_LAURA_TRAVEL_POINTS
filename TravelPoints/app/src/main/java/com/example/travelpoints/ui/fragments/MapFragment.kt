package com.example.travelpoints.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelpoints.adapters.SearchViewAdapter
import com.example.travelpoints.databinding.FragmentMapBinding
import com.example.travelpoints.helpers.LocationPermission
import com.example.travelpoints.models.Site
import com.example.travelpoints.ui.viewmodels.MapFragmentViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapFragment(
    private val navigateToSiteCreation: (Double, Double) -> Unit,
    private val navigateToSiteDetails: (Site) -> Unit,
) : Fragment(),
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var locationRequest: LocationRequest
    private val viewModel by lazy {
        ViewModelProvider(this)[MapFragmentViewModel::class.java]
    }
    var siteToPositionAt: Site? = null

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
                adapter.dataList = sites.map { Pair(it.name, LatLng(it.latitude, it.longitude)) }
            } else {
                adapter.dataList = emptyList()
            }
        }

        binding.closeBtn.setOnClickListener {
            binding.constraintLayout.visibility = View.VISIBLE
            binding.siteDetails.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle search query submission
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText.isNotBlank()) {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.constraintLayout.visibility = View.INVISIBLE
                    binding.siteDetails.visibility = View.INVISIBLE

                    val newList = mutableSetOf<Pair<String, LatLng>>()
                    viewModel.sites.value?.forEach {
                        if (it.name.lowercase().contains(newText.lowercase())) {
                            newList.add(Pair(it.name, LatLng(it.latitude, it.longitude)))
                        }
                    }
                    adapter.dataList = newList.toList()

                } else {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.constraintLayout.visibility = View.VISIBLE
                    binding.siteDetails.visibility = View.INVISIBLE
                }

                return false
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        LocationPermission(
            moveToUserLocation = {
                if (siteToPositionAt == null) {
                    moveToUserLocation(it)
                } else {
                    showSiteDetailsPopUp(siteToPositionAt!!)
                    onSiteClicked(LatLng(siteToPositionAt!!.latitude, siteToPositionAt!!.longitude))
                    siteToPositionAt = null

                }
            }
        ).getCurrentLocation(requireActivity(), locationRequest, map)

        map.setOnMarkerClickListener(this)

        map.setOnMapLongClickListener {
            //TODO allow site creation only if logged in user is ADMIN
            //if (FirebaseAuth.getInstance().currentUser?.uid.equals("LWtquFDJ0MWlXqTc6ZsukLAklvb2")) {
            navigateToSiteCreation(it.latitude, it.longitude)
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        viewModel.sites.observe(viewLifecycleOwner) { sites ->
            sites?.forEach {
                if (p0.position.latitude == it.latitude && p0.position.longitude == it.longitude) {
                    showSiteDetailsPopUp(it)
                }
            }
        }
        return true
    }

    private fun showSiteDetailsPopUp(site: Site) {
        binding.siteNameTv.text = site.name
        binding.descriptionTv.text = site.description
        binding.entryPriceTv.text = site.entryPrice.toString()

        binding.constraintLayout.visibility = View.INVISIBLE
        binding.siteDetails.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE

        binding.seeMoreBtn.setOnClickListener { navigateToSiteDetails(site) }
    }

    private fun onSiteClicked(latLng: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        map.animateCamera(CameraUpdateFactory.zoomIn())
        map.animateCamera(CameraUpdateFactory.zoomTo(13f))
    }

    private fun moveToUserLocation(userLatLng: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,12f))
        map.animateCamera(CameraUpdateFactory.zoomIn())
        map.animateCamera(CameraUpdateFactory.zoomTo(12f))
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