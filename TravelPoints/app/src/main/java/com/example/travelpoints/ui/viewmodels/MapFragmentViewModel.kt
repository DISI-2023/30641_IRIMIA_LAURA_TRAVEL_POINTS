package com.example.travelpoints.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapFragmentViewModel: ViewModel() {

    val sites = MutableLiveData<Set<Pair<String, LatLng>>>(null)

    fun getAllSites() {
        val newSites = mutableSetOf<Pair<String, LatLng>>()
        val siteNumber = FirebaseDatabase.getInstance().getReference("Sites")
        siteNumber.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val latitude = it.child("Location").child("Latitude").getValue(Double::class.java)
                        val longitude = it.child("Location").child("Longitude").getValue(Double::class.java)
                        val name = it.child("Name").value.toString()

                        if(latitude!=null && longitude!=null && name!=null){
                            newSites.add(Pair(name, LatLng(latitude,longitude)))
                        }

                    }
                    sites.postValue(newSites)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}