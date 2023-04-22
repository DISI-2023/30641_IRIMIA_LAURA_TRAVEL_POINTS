package com.example.travelpoints.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelpoints.models.Site

@Composable
fun SiteDetailsView(
    site: Site,
    onScreenClose: () -> Unit
) {

    BackHandler(onBack = onScreenClose)
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = site.name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontSize = 22.sp
            )
        }, backgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White, navigationIcon = {
            IconButton(onClick = onScreenClose) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = null, tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(24.dp)
        ) {
            item {
                val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Description:", color = textColor)
                    Text(text = site.description, color = textColor)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "EntryPrice:", color = textColor)
                    Text(text = site.entryPrice.toString(), color = textColor)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Category:", color = textColor)
                    Text(text = site.category.toString(), color = textColor)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Latitude:", color = textColor)
                    Text(text = site.latitude.toString(), color = textColor)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Longitude:", color = textColor)
                    Text(text = site.longitude.toString(), color = textColor)
                }
            }
        }
    }
}