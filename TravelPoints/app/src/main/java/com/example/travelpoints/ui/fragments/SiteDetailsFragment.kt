package com.example.travelpoints.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.travelpoints.models.Site
import com.example.travelpoints.ui.theme.TravelPointsTheme
import com.example.travelpoints.ui.views.AccountView
import com.example.travelpoints.ui.views.SiteDetailsView

class SiteDetailsFragment(
    private val site: Site,
    private val navigateToMapFragment: () -> Unit
): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TravelPointsTheme {
                    SiteDetailsView(site = site, onScreenClose = navigateToMapFragment)
                }
            }
        }
    }
}