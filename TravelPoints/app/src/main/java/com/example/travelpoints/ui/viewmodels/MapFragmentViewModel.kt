package com.example.travelpoints.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelpoints.models.Site
import com.example.travelpoints.models.fromStringToCategory
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapFragmentViewModel: ViewModel() {

    val sites = MutableLiveData<List<Site>>(null)

    fun getAllSites() {
        val newSites = mutableListOf<Site>()
        val siteNumber = FirebaseDatabase.getInstance().getReference("Sites")
        siteNumber.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val latitude = it.child("Location").child("Latitude").getValue(Double::class.java)
                        val longitude = it.child("Location").child("Longitude").getValue(Double::class.java)
                        val name = it.child("Name").value.toString()
                        val id = it.child("ID").getValue(Long::class.java)
                        val entryPrice = it.child("EntryPrice").getValue(Double::class.java)
                        val description = it.child("Description").value.toString()
                        val category = fromStringToCategory(it.child("Category").value.toString())
                        val offerValue = it.child("OfferValue").getValue(Double::class.java) ?: 0.0

                        if(latitude!=null && longitude!=null && name!=null && id!=null && entryPrice!=null && description!=null && category!=null){
                            val newSite = Site(
                                id,
                                name,
                                latitude,
                                longitude,
                                entryPrice,
                                description,
                                category,
                                offerValue
                            )
                            newSites.add(newSite)
                        }

                    }
                    sites.postValue(newSites)
                    Site.allSites = newSites
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}