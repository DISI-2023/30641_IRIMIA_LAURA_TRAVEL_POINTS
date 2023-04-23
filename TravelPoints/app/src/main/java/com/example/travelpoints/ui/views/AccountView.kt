package com.example.travelpoints.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelpoints.models.Site
import com.example.travelpoints.ui.viewmodels.AccountViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

@Composable
fun AccountView(
    navigateToLoginFragment: () -> Unit,
    navigateToSiteDetails: (Site) -> Unit,
    viewModel: AccountViewModel
) {

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val wishlistSites = viewModel.wishlistSites.collectAsState()

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "My Wishlist",
            fontSize = 22.sp,
            color = MaterialTheme.colors.primary
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            backgroundColor = MaterialTheme.colors.background,
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(0.8f).padding(8.dp)) {
                item {
                    wishlistSites.value.forEach {
                        OutlinedButton(onClick = { navigateToSiteDetails(it) }) {
                            Text(text = it.name)
                        }
                    }
                }
            }

        }

        OutlinedButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navigateToLoginFragment()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Log Out", color = textColor)
        }
    }
}
