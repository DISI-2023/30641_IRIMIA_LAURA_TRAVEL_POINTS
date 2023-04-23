package com.example.travelpoints.ui.views

import android.util.Log
import android.widget.RatingBar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelpoints.models.Site
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.example.travelpoints.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.core.graphics.applyCanvas
import com.example.travelpoints.ui.viewmodels.SiteDetailsViewModel

@Composable
fun SiteDetailsView(
    site: Site,
    viewModel: SiteDetailsViewModel = SiteDetailsViewModel(site),
    onScreenClose: () -> Unit
) {

    BackHandler(onBack = onScreenClose)
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = site.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 22.sp
                )
            },
            backgroundColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
            navigationIcon = {
                IconButton(onClick = onScreenClose) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
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

                SiteAverageRating(
                    rating = viewModel.averageRating.collectAsState().value
                )

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
                val currentRating = viewModel.currentRating.collectAsState()
                RatingBar(
                    currentRating = currentRating.value,
                    saveRating = {
                        viewModel.updateCurrentRating(it)
                        viewModel.saveRatingToFirebase(it)
                    }
                )
                WishlistOption(viewModel)
            }
        }
    }
}

@Composable
private fun RatingBar(
    currentRating: Int,
    saveRating: (Int) -> Unit,
) {
    Row {
        StarImage(
            index = 1,
            currentRating = currentRating
        ) {
            saveRating(it)
        }
        StarImage(
            index = 2,
            currentRating = currentRating
        ) {
            saveRating(it)
        }
        StarImage(
            index = 3,
            currentRating = currentRating
        ) {
            saveRating(it)
        }
        StarImage(
            index = 4,
            currentRating = currentRating
        ) {
            saveRating(it)
        }
        StarImage(
            index = 5,
            currentRating = currentRating
        ) {
            saveRating(it)
        }
    }
}

@Composable
private fun StarImage(
    index: Int,
    currentRating: Int,
    onClick: (Int) -> Unit
) {
    Image(
        painter = if (currentRating >= index) painterResource(id = R.drawable.ic_star_full_2) else painterResource(
            id = R.drawable.ic_star_empty_2
        ),
        contentDescription = null,
        modifier = Modifier
            .padding(end = 2.dp)
            .clickable {
                onClick(index)
            }
    )
}

@Composable
private fun SiteAverageRating(
    rating: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rating.toString()
        )
        Image(
            painter = painterResource(id = R.drawable.ic_star_full_2),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 2.dp)
        )
    }
}

@Composable
private fun WishlistOption(
    viewModel: SiteDetailsViewModel
) {
    val isInWishlist = viewModel.isInWishlist.collectAsState()

    OutlinedButton(
        onClick = { viewModel.updateIsInWishlist(!isInWishlist.value) },
        border = BorderStroke(1.dp, Color.Red)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (isInWishlist.value) painterResource(id = R.drawable.ic_heart_full) else painterResource(
                    id = R.drawable.ic_heart_empty
                ), contentDescription = null
            )
            Text(
                text = if (isInWishlist.value) "Added to Wishlist" else "Add to Wishlist",
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}