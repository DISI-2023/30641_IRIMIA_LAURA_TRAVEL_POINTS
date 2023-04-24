package com.example.travelpoints.ui.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelpoints.models.Site
import com.example.travelpoints.ui.viewmodels.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

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
                modifier = Modifier
                    .weight(0.8f)
                    .padding(8.dp)
            ) {
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
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Log Out", color = textColor)
        }
    }
}
