package com.example.nelo.presentation.screen.library

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nelo.MainViewModel
import com.example.nelo.data.library.LibraryViewModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.navigation.DetailNavScreens
import com.example.nelo.presentation.navigation.RootNavGraphs

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mLibraryViewModel: LibraryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(
        LibraryViewModel::class.java)

    val toggleTheme = mainViewModel.toggleTheme.value
    val library by mLibraryViewModel.mangaLibrary.collectAsState()

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White

    LaunchedEffect(true) {
        mLibraryViewModel.getLibrary()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Library",
                        color = themeBackgroundColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = themeTextColor,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        containerColor = themeBackgroundColor,
        content = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(top = 55.dp, bottom = 50.dp)
            ) {
                items(library) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(225f / 337f)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (navController.currentBackStackEntry?.destination?.route != DetailNavScreens.MangaScreen.route) {
                                    mainViewModel._mangaDetail.value =
                                        MangaModel(
                                            title = it.mangaTitle,
                                            thumbnail = it.mangaThumbnail,
                                            authors = emptyList(),
                                            genres = emptyList(),
                                            status = null,
                                            updatedAt = null,
                                            description = null,
                                            view = null,
                                            rating = null,
                                            mangaUrl = it.mangaUrl,
                                            chapterList = emptyList()
                                        )
                                    //mainViewModel.getManga()
                                    navController.navigate(RootNavGraphs.DetailGraph.route)
                                }
                            }
                    ) {
                        AsyncImage(
                            model = it.mangaThumbnail,
                            contentDescription = "Manga Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.6f)
                                        ),
                                        startY = (size.height / 4) + (size.height / 2),
                                        endY = size.height
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(
                                            gradient,
                                            blendMode = BlendMode.Multiply
                                        )
                                    }
                                }
                        )

                        Text(
                            text = it.mangaTitle,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(6.dp),
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun LibraryScreenPreview() {
    LibraryScreen(mainViewModel = MainViewModel(), navController = rememberNavController())
}