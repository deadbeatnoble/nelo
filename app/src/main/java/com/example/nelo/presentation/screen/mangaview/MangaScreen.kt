package com.example.nelo.presentation.screen.mangaview

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.data.history.HistoryViewModel
import com.example.nelo.data.library.LibraryEntity
import com.example.nelo.data.library.LibraryViewModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.navigation.BottomNavScreens
import com.example.nelo.presentation.navigation.DetailNavScreens
import com.example.nelo.util.UiState

@Composable
fun MangaScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    mangaDetailsViewModel: MangaDetailsViewModel
) {

    val uiState by mangaDetailsViewModel.uiState.collectAsState()



    val mHistoryViewModel: HistoryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(
        HistoryViewModel::class.java)
    val mLibraryViewModel: LibraryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(
        LibraryViewModel::class.java)

    val mangaDetailLoading = mainViewModel.mangaDetailLoading.value
    val manga = mainViewModel.mangaDetail.value
    val mangaDetailError = mainViewModel.mangaDetailError.value

    val toggleTheme = mainViewModel.toggleTheme.value
    val toggleFavorite = mLibraryViewModel.existsLibraryState.value

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White
    val theme = if (toggleTheme) R.drawable.light else R.drawable.dark
    val heart = if (!toggleFavorite) R.drawable.outline_heart else R.drawable.filled_heart
    val people = if (toggleTheme) R.drawable.outline_people else R.drawable.filled_people
    val ongoing = if (toggleTheme) R.drawable.outline_ongoing else R.drawable.filled_ongoing
    val completed = if (toggleTheme) R.drawable.outline_completed else R.drawable.filled_completed
    val view = if (toggleTheme) R.drawable.outline_eye else R.drawable.filled_eye

    LaunchedEffect(true) {
        mangaDetailsViewModel.getMangaDetails(mangaUrl = mainViewModel.mangaDetail.value.mangaUrl!!)
        mLibraryViewModel.existState(mangaUrl = manga.mangaUrl!!)
    }

    when(uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }
        is UiState.Success -> {
            val mangaDetail = (uiState as UiState.Success<MangaModel>).data

            val status = if (mangaDetail.status == "ongoing") ongoing else completed
            val authors = mutableListOf<String>()
            for (a in mangaDetail.authors) {
                authors.add(a.name ?: "")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackgroundColor)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(266.dp)
                    ) {
                        AsyncImage(
                            model = mangaDetail.thumbnail,
                            contentDescription = "Background cover art image",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter,
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                setToSaturation(
                                    0f
                                )
                            }),
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            themeBackgroundColor.copy(alpha = 1f)
                                        )
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient)
                                    }
                                }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.backarrow),
                                        contentDescription = "Navigate back",
                                        tint = themeTextColor,
                                        modifier = Modifier
                                            .size(25.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    IconButton(
                                        onClick = {
                                            mLibraryViewModel.onLibraryClicked(
                                                libraryEntity =
                                                LibraryEntity(
                                                    id = 0,
                                                    mangaTitle = mangaDetail.title!!,
                                                    mangaThumbnail = mangaDetail.thumbnail!!,
                                                    mangaUrl = mangaDetail.mangaUrl!!
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = heart),
                                            contentDescription = "Add to favorite",
                                            tint = colorResource(id = R.color.red),
                                            modifier = Modifier
                                                .size(25.dp)
                                        )
                                    }

                                    IconButton(onClick = {
                                        mainViewModel._toggleTheme.value = !toggleTheme
                                    }) {
                                        Icon(
                                            painter = painterResource(id = theme),
                                            contentDescription = "Toggle Night/Dark mode",
                                            tint = colorResource(R.color.orange),
                                            modifier = if (toggleTheme) {
                                                Modifier
                                                    .size(25.dp)
                                            } else {
                                                Modifier
                                                    .size(25.dp)
                                                    .clip(RoundedCornerShape(50.dp))
                                                    .background(color = colorResource(R.color.dark_blue))
                                                    .padding(2.dp)
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(175.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                AsyncImage(
                                    model = mangaDetail.thumbnail,
                                    contentDescription = "Thumbnail",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(130.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    AutoResizableText(
                                        customText = mangaDetail.title ?: "Unknown",
                                        customFontSize = 18.sp,
                                        customFontWeight = FontWeight.SemiBold,
                                        themeTextColor = themeTextColor,
                                        customMaxLines = 4
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                painter = painterResource(id = people),
                                                contentDescription = "Author icon",
                                                tint = themeTextColor.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            AutoResizableText(
                                                customText = if(authors.isEmpty()) "Updating..." else authors.joinToString(" , "),
                                                customFontSize = 14.sp,
                                                customFontWeight = FontWeight.Normal,
                                                themeTextColor = themeTextColor.copy(alpha = 0.6f),
                                                customMaxLines = 3,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (authors.isNotEmpty()) {
                                                            navController.navigate(BottomNavScreens.Browse.route) {
                                                                popUpTo(navController.graph.findStartDestination().id)
                                                                launchSingleTop = true
                                                            }

                                                            mainViewModel._selectedTab.value =
                                                                "Filter"
                                                            mainViewModel.feedChange.value = true
                                                            mainViewModel._feedResponse.value.clear()
                                                            mainViewModel._hasNextPage.value = true
                                                            mainViewModel._currentPage.value = 1

                                                            mainViewModel.keyword.value =
                                                                authors.joinToString(", ")
                                                            mainViewModel.selectedKeyword.value =
                                                                "Author"

                                                            authors.forEachIndexed { index, author ->
                                                                val updatedAuthor =
                                                                    author.replace(" ", "_")
                                                                authors[index] = updatedAuthor
                                                            }


                                                            val url =
                                                                "https://manganato.com/advanced_search?s=all&page=${mainViewModel._currentPage.value}&keyt=author&keyw=${
                                                                    authors.joinToString("_")
                                                                }"

                                                            mainViewModel.advancedSearchUrl.value =
                                                                url
                                                            mainViewModel.getAdvancedSearchFeed()
                                                        }
                                                    }
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = status),
                                                contentDescription = "Status icon",
                                                tint = themeTextColor.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = mangaDetail.status ?: "Unknown",
                                                color = themeTextColor.copy(alpha = 0.6f),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }

                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { i ->
                                if (((mangaDetail.rating?.toDouble() ?: 0.0) - i) >= 1) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.filled_star),
                                        contentDescription = "Full star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                } else if (((mangaDetail.rating?.toDouble() ?: 0.0) - i) >= 0.5) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.half_star),
                                        contentDescription = "Half star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_star),
                                        contentDescription = "Empty star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                }
                            }
                        }

                        Row {
                            Icon(
                                painter = painterResource(id = view),
                                contentDescription = "View icon",
                                tint = themeTextColor.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = mangaDetail.view ?: "??",
                                color = themeTextColor.copy(alpha = 0.6f),
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                item {
                    ExtendableText(
                        description = mangaDetail.description ?: "",
                        themeTextColor = themeTextColor,
                        themeBackgroundColor = themeBackgroundColor
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(mangaDetail.genres) {
                            Text(
                                text = it.name ?: "",
                                color = themeTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = themeTextColor,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .clickable {
                                        navController.navigate(BottomNavScreens.Browse.route) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }

                                        mainViewModel._selectedTab.value = "Filter"
                                        mainViewModel.feedChange.value = true
                                        mainViewModel._feedResponse.value.clear()
                                        mainViewModel._hasNextPage.value = true
                                        mainViewModel._currentPage.value = 1

                                        val url =
                                            "https://manganato.com/advanced_search?s=all&g_i=_${it.id}_&page=${mainViewModel._currentPage.value}"
                                        //"https://manganato.com/genre-${it.id}/${mainViewModel._currentPage.value}"

                                        mainViewModel.advancedSearchUrl.value = url
                                        mainViewModel.getAdvancedSearchFeed()
                                    }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }

                items(mangaDetail.chapterList) {
                    mainViewModel.onUpdate.value
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                mainViewModel._chapterDetail.value = it
                                mainViewModel.getChapter()
                                navController.navigate(DetailNavScreens.ChapterScreen.route)
                                mainViewModel.updateUI()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.title ?: "",
                                color = if(mHistoryViewModel.existHistory(mangaChapterUrl = it.chapterUrl!!)) themeTextColor.copy(alpha = 0.6f) else themeTextColor,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                            )

                            Row {
                                Icon(
                                    painter = painterResource(id = view),
                                    contentDescription = "View icon",
                                    tint = themeTextColor.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = it.view ?: "??",
                                    color = themeTextColor.copy(alpha = 0.6f),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Text(
                            text = it.uploadedAt ?: "",
                            color = themeTextColor.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(horizontal = 4.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }
            }
        }
        is UiState.Error -> {
            Text(text = (uiState as UiState.Error).message)
        }
    }

    /*if (mangaDetailLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (mangaDetailError.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackgroundColor)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = mangaDetailError,
                        fontSize = 18.sp,
                        color = themeTextColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            mainViewModel.getManga()
                        }
                    ) {
                        Text(
                            text = "Retry"
                        )
                    }
                }
            }
        } else {
            val status = if (manga.status == "ongoing") ongoing else completed
            val authors = mutableListOf<String>()
            for (a in manga.authors) {
                authors.add(a.name ?: "")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackgroundColor)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(266.dp)
                    ) {
                        AsyncImage(
                            model = manga.thumbnail,
                            contentDescription = "Background cover art image",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter,
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                                setToSaturation(
                                    0f
                                )
                            }),
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithCache {
                                    val gradient = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            themeBackgroundColor.copy(alpha = 1f)
                                        )
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient)
                                    }
                                }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.backarrow),
                                        contentDescription = "Navigate back",
                                        tint = themeTextColor,
                                        modifier = Modifier
                                            .size(25.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    IconButton(
                                        onClick = {
                                            mLibraryViewModel.onLibraryClicked(
                                                libraryEntity =
                                                    LibraryEntity(
                                                        id = 0,
                                                        mangaTitle = manga.title!!,
                                                        mangaThumbnail = manga.thumbnail!!,
                                                        mangaUrl = manga.mangaUrl!!
                                                    )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = heart),
                                            contentDescription = "Add to favorite",
                                            tint = colorResource(id = R.color.red),
                                            modifier = Modifier
                                                .size(25.dp)
                                        )
                                    }

                                    IconButton(onClick = {
                                        mainViewModel._toggleTheme.value = !toggleTheme
                                    }) {
                                        Icon(
                                            painter = painterResource(id = theme),
                                            contentDescription = "Toggle Night/Dark mode",
                                            tint = colorResource(R.color.orange),
                                            modifier = if (toggleTheme) {
                                                Modifier
                                                    .size(25.dp)
                                            } else {
                                                Modifier
                                                    .size(25.dp)
                                                    .clip(RoundedCornerShape(50.dp))
                                                    .background(color = colorResource(R.color.dark_blue))
                                                    .padding(2.dp)
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(175.dp)
                                    .padding(horizontal = 16.dp)
                            ) {
                                AsyncImage(
                                    model = manga.thumbnail,
                                    contentDescription = "Thumbnail",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .width(130.dp)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    AutoResizableText(
                                        customText = manga.title ?: "Unknown",
                                        customFontSize = 18.sp,
                                        customFontWeight = FontWeight.SemiBold,
                                        themeTextColor = themeTextColor,
                                        customMaxLines = 4
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                painter = painterResource(id = people),
                                                contentDescription = "Author icon",
                                                tint = themeTextColor.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            AutoResizableText(
                                                customText = if(authors.isEmpty()) "Updating..." else authors.joinToString(" , "),
                                                customFontSize = 14.sp,
                                                customFontWeight = FontWeight.Normal,
                                                themeTextColor = themeTextColor.copy(alpha = 0.6f),
                                                customMaxLines = 3,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (authors.isNotEmpty()) {
                                                            navController.navigate(BottomNavScreens.Browse.route) {
                                                                popUpTo(navController.graph.findStartDestination().id)
                                                                launchSingleTop = true
                                                            }

                                                            mainViewModel._selectedTab.value =
                                                                "Filter"
                                                            mainViewModel.feedChange.value = true
                                                            mainViewModel._feedResponse.value.clear()
                                                            mainViewModel._hasNextPage.value = true
                                                            mainViewModel._currentPage.value = 1

                                                            mainViewModel.keyword.value =
                                                                authors.joinToString(", ")
                                                            mainViewModel.selectedKeyword.value =
                                                                "Author"

                                                            authors.forEachIndexed { index, author ->
                                                                val updatedAuthor =
                                                                    author.replace(" ", "_")
                                                                authors[index] = updatedAuthor
                                                            }


                                                            val url =
                                                                "https://manganato.com/advanced_search?s=all&page=${mainViewModel._currentPage.value}&keyt=author&keyw=${
                                                                    authors.joinToString("_")
                                                                }"

                                                            mainViewModel.advancedSearchUrl.value =
                                                                url
                                                            mainViewModel.getAdvancedSearchFeed()
                                                        }
                                                    }
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = status),
                                                contentDescription = "Status icon",
                                                tint = themeTextColor.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = manga.status ?: "Unknown",
                                                color = themeTextColor.copy(alpha = 0.6f),
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }

                                }
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { i ->
                                if (((manga.rating?.toDouble() ?: 0.0) - i) >= 1) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.filled_star),
                                        contentDescription = "Full star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                } else if (((manga.rating?.toDouble() ?: 0.0) - i) >= 0.5) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.half_star),
                                        contentDescription = "Half star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_star),
                                        contentDescription = "Empty star",
                                        tint = colorResource(id = R.color.orange)
                                    )
                                }
                            }
                        }

                        Row {
                            Icon(
                                painter = painterResource(id = view),
                                contentDescription = "View icon",
                                tint = themeTextColor.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = manga.view ?: "??",
                                color = themeTextColor.copy(alpha = 0.6f),
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                item {
                    ExtendableText(
                        description = manga.description ?: "",
                        themeTextColor = themeTextColor,
                        themeBackgroundColor = themeBackgroundColor
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        items(manga.genres) {
                            Text(
                                text = it.name ?: "",
                                color = themeTextColor,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = themeTextColor,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .clickable {
                                        navController.navigate(BottomNavScreens.Browse.route) {
                                            popUpTo(navController.graph.findStartDestination().id)
                                            launchSingleTop = true
                                        }

                                        mainViewModel._selectedTab.value = "Filter"
                                        mainViewModel.feedChange.value = true
                                        mainViewModel._feedResponse.value.clear()
                                        mainViewModel._hasNextPage.value = true
                                        mainViewModel._currentPage.value = 1

                                        val url =
                                            "https://manganato.com/advanced_search?s=all&g_i=_${it.id}_&page=${mainViewModel._currentPage.value}"
                                        //"https://manganato.com/genre-${it.id}/${mainViewModel._currentPage.value}"

                                        mainViewModel.advancedSearchUrl.value = url
                                        mainViewModel.getAdvancedSearchFeed()
                                    }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }

                items(manga.chapterList) {
                    mainViewModel.onUpdate.value
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                mainViewModel._chapterDetail.value = it
                                mainViewModel.getChapter()
                                navController.navigate(DetailNavScreens.ChapterScreen.route)
                                mainViewModel.updateUI()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.title ?: "",
                                color = if(mHistoryViewModel.existHistory(mangaChapterUrl = it.chapterUrl!!)) themeTextColor.copy(alpha = 0.6f) else themeTextColor,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                            )

                            Row {
                                Icon(
                                    painter = painterResource(id = view),
                                    contentDescription = "View icon",
                                    tint = themeTextColor.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = it.view ?: "??",
                                    color = themeTextColor.copy(alpha = 0.6f),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Text(
                            text = it.uploadedAt ?: "",
                            color = themeTextColor.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(horizontal = 4.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                }
            }
        }
    }*/

}

@Composable
fun AutoResizableText(
    customText: String,
    customFontSize: TextUnit,
    customFontWeight: FontWeight,
    themeTextColor: Color,
    customMaxLines: Int,
    modifier: Modifier = Modifier
) {
    var textStyle by remember {
        mutableStateOf(
            TextStyle(
                fontSize = customFontSize,
                fontWeight = customFontWeight
            )
        )
    }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = customText,
        style = textStyle,
        color = themeTextColor,
        maxLines = customMaxLines,
        overflow = TextOverflow.Clip,
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                if (readyToDraw) drawContent()
            },
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowHeight) {
                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
            } else {
                readyToDraw = true
            }
        })
}

@Composable
fun ExtendableText(
    description: String,
    themeTextColor: Color,
    themeBackgroundColor: Color
) {
    var showMore by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .animateContentSize(animationSpec = tween(100))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { showMore = !showMore }
    ) {
        if (description.toCharArray().size > 50) {
            if (showMore) {
                Text(
                    text = description,
                    color = themeTextColor,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        text = description,
                        color = themeTextColor,
                        fontSize = 14.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawWithCache {
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        themeBackgroundColor.copy(alpha = 0.9f)
                                    ),
                                    startY = size.height / 3,
                                    endY = size.height
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(gradient, blendMode = BlendMode.Multiply)
                                }
                            }

                    )

                    Icon(
                        painter = painterResource(id = R.drawable.more),
                        contentDescription = "See more",
                        tint = themeTextColor,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        } else {
            Text(
                text = description,
                color = themeTextColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MangaScreenPreview() {
    MangaScreen(navController = rememberNavController(), mainViewModel = MainViewModel(), mangaDetailsViewModel = hiltViewModel())
}