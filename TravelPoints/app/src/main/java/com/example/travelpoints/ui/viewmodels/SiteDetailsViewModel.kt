package com.example.travelpoints.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.travelpoints.models.Site
import com.example.travelpoints.models.getActiveUserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SiteDetailsViewModel(
    private val site: Site
) : ViewModel() {

    private val _currentRating: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentRating = _currentRating.asStateFlow()

    private val _averageRating: MutableStateFlow<Float> = MutableStateFlow(0f)
    val averageRating = _averageRating.asStateFlow()

    private val _isInWishlist: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInWishlist = _isInWishlist.asStateFlow()

    init {
        getRatingOfCurrentUser()
        getAverageRating()
        checkIfIsInWishlist()
    }

    private fun getRatingOfCurrentUser() {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Ratings")
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val siteId = it.child("SiteId").getValue(Long::class.java)
                        val userId = it.child("UserID").value.toString()
                        if (siteId == site.id && userId == getActiveUserId()) {
                            val rating = it.child("Rating").getValue(Int::class.java)
                            if (rating != null) {
                                _currentRating.value = rating
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun saveRatingToFirebase(rating: Int) {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Ratings")
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var ratingAlreadyExists = false
                    snapshot.children.forEach {
                        val siteId = it.child("SiteId").getValue(Long::class.java)
                        val userId = it.child("UserID").value.toString()
                        if (siteId == site.id && userId == getActiveUserId()) {
                            ratingAlreadyExists = true
                            firebaseReference.child(it.key.toString()).child("Rating")
                                .setValue(rating)
                        }
                    }
                    if (!ratingAlreadyExists) {
                        addNewRating(rating)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun addNewRating(rating: Int) {
        val ratingsNumber = FirebaseDatabase.getInstance().getReference("RatingsNumber").child("ID")
        ratingsNumber.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentId = (snapshot.value as Long) + 1L
                    val firebaseReference =
                        FirebaseDatabase.getInstance().getReference("Ratings").child("$currentId")
                    firebaseReference.child("Rating").setValue(rating)
                    firebaseReference.child("UserID")
                        .setValue(getActiveUserId())
                    firebaseReference.child("SiteId")
                        .setValue(site.id)
                    ratingsNumber.setValue(currentId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getAverageRating() {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Ratings")
        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalRatings = 0
                var ratingsNumber = 0
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val siteId = it.child("SiteId").getValue(Long::class.java)
                        if (siteId == site.id) {
                            val rating = it.child("Rating").getValue(Int::class.java)
                            if (rating != null) {
                                totalRatings += rating
                                ratingsNumber++
                            }
                        }
                    }
                }
                _averageRating.value = totalRatings.toFloat() / ratingsNumber.toFloat()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun updateCurrentRating(rating: Int) {
        _currentRating.value = rating

    }

    private fun checkIfIsInWishlist() {
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Wishlist").child(getActiveUserId().toString())
        firebaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    snapshot.children.forEach {
                        if (it.key == site.id.toString()) {
                            _isInWishlist.value = it.getValue(Boolean::class.java) ?: false
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun updateIsInWishlist(newValue: Boolean) {
        _isInWishlist.value = newValue
        val firebaseReference = FirebaseDatabase.getInstance().getReference("Wishlist").child(getActiveUserId().toString())
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (newValue) {
                        firebaseReference.child(site.id.toString()).setValue(true)
                    } else {
                        firebaseReference.child(site.id.toString()).setValue(false)
                    }
                } else {
                    firebaseReference.setValue(0)
                    if (newValue) {
                        firebaseReference.child(site.id.toString()).setValue(true)
                    } else {
                        firebaseReference.child(site.id.toString()).setValue(false)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}