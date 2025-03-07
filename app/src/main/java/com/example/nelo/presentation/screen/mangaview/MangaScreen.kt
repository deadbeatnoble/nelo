package com.example.nelo.presentation.screen.mangaview

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.presentation.viewmodels.HistoryViewModel
import com.example.nelo.data.model.LibraryEntity
import com.example.nelo.presentation.viewmodels.LibraryViewModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.navigation.BottomNavScreens
import com.example.nelo.presentation.navigation.DetailNavScreens
import com.example.nelo.presentation.screen.browse.ErrorDialog
import com.example.nelo.presentation.screen.browse.LoadingDialog
import com.example.nelo.presentation.viewmodels.SharedViewModel
import com.example.nelo.util.UiState

@Composable
fun MangaScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    sharedViewModel: SharedViewModel,
    historyViewModel: HistoryViewModel,
    libraryViewModel: LibraryViewModel
) {

    val uiState by sharedViewModel.mangaDetailsUiState.collectAsState()



    //val mHistoryViewModel: HistoryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(HistoryViewModel::class.java)
    //val mLibraryViewModel: LibraryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(LibraryViewModel::class.java)

    //val mangaDetailLoading = mainViewModel.mangaDetailLoading.value
    val manga = mainViewModel.mangaDetail.value
    //val mangaDetailError = mainViewModel.mangaDetailError.value

    val toggleTheme = mainViewModel.toggleTheme.value
    val toggleFavorite = libraryViewModel.existsLibraryState.value

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White
    val theme = if (toggleTheme) R.drawable.light else R.drawable.dark
    val heart = if (!toggleFavorite) R.drawable.outline_heart else R.drawable.filled_heart
    val people = if (toggleTheme) R.drawable.outline_people else R.drawable.filled_people
    val ongoing = if (toggleTheme) R.drawable.outline_ongoing else R.drawable.filled_ongoing
    val completed = if (toggleTheme) R.drawable.outline_completed else R.drawable.filled_completed
    val view = if (toggleTheme) R.drawable.outline_eye else R.drawable.filled_eye

    LaunchedEffect(true) {
        sharedViewModel.getMangaDetails(mangaUrl = mainViewModel.mangaDetail.value.mangaUrl!!)
        libraryViewModel.existState(mangaUrl = manga.mangaUrl!!)
    }

    when(uiState) {
        is UiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 32.dp)
            ) {
                LoadingDialog()
            }
        }
        is UiState.Success -> {
            val mangaDetail = (uiState as UiState.Success<MangaModel>).data

            val status = if (mangaDetail.status == "ongoing") ongoing else completed
            val authors = mutableListOf<String>()
            for (a in mangaDetail.authors) {
                authors.add(a.name ?: "")
            }

            NeloMangaScreen(
                navController = navController,
                libraryViewModel = libraryViewModel,
                mangaDetail = mangaDetail,
                mainViewModel = mainViewModel,
                historyViewModel = historyViewModel,
                authors = authors
            )
        }
        is UiState.Error -> {
            //Text(text = (uiState as UiState.Error).message)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 32.dp)

            ) {
                val errorMessage = (uiState as UiState.Error).message
                Spacer(modifier = Modifier.height(50.dp))
                ErrorDialog(
                    errorMessage = errorMessage,
                    onRetryClicked = {
                        sharedViewModel.getMangaDetails(mangaUrl = mainViewModel.mangaDetail.value.mangaUrl!!)
                        libraryViewModel.existState(mangaUrl = manga.mangaUrl!!)
                    }
                )
            }
        }
    }

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
        textAlign = TextAlign.Center,
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NeloMangaScreen(
    navController: NavHostController,
    libraryViewModel: LibraryViewModel,
    mangaDetail: MangaModel,
    mainViewModel: MainViewModel,
    historyViewModel: HistoryViewModel,
    authors: MutableList<String>
) {
    val sheetState = rememberBottomSheetScaffoldState()
    val state = rememberLazyListState()
    val showScrollbar = remember { mutableStateOf(false) }
    val scrollBarColor = colorResource(id = R.color.red_bg)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = mangaDetail.title ?: "Unknown",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        ),
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(48.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(48.dp))
                            .background(colorResource(id = R.color.blue_bg))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow_icon),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            libraryViewModel.onLibraryClicked(
                                libraryEntity =
                                LibraryEntity(
                                    id = 0,
                                    mangaTitle = mangaDetail.title!!,
                                    mangaThumbnail = mangaDetail.thumbnail!!,
                                    mangaUrl = mangaDetail.mangaUrl!!
                                )
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(48.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(48.dp))
                            .background(colorResource(id = R.color.red_bg))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outlined_bookmark),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    ) { paddingValues ->
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
            sheetPeekHeight = 330.dp,
            sheetContent = {
                Surface(
                    modifier = Modifier
                        .heightIn(max = 700.dp)
                        .border(
                            2.dp,
                            Color.Black,
                            RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .padding(start = 130.dp, end = 130.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.Black)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "All Chapters (${mangaDetail.chapterList.size})",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            state = state,
                            modifier = Modifier
                                .drawBehind {
                                    val firstVisibleItemIndex = state.firstVisibleItemIndex
                                    val totalItems = mangaDetail.chapterList.size
                                    val visibleItems = 10 // Approximate visible items

                                    if (totalItems > visibleItems) {
                                        showScrollbar.value = true
                                    }

                                    if (showScrollbar.value) {
                                        val scrollbarHeight =
                                            size.height * (visibleItems / totalItems.toFloat())
                                        val scrollbarY =
                                            size.height * (firstVisibleItemIndex / totalItems.toFloat())

                                        drawRoundRect(
                                            color = scrollBarColor,
                                            topLeft = Offset(size.width - 4.dp.toPx(), scrollbarY),
                                            size = Size(4.dp.toPx(), scrollbarHeight),
                                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                                        )
                                    }
                                }
                        ) {
                            items(mangaDetail.chapterList) {
                                mainViewModel.onUpdate.value
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier
                                        .padding(top = 16.dp, end = 8.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            mainViewModel._chapterDetail.value = it

                                            mainViewModel._mangaDetail.value = mangaDetail

                                            //mainViewModel.getChapter()

                                            navController.navigate("${DetailNavScreens.ChapterScreen.route}?chapterUrl=${it.chapterUrl}&chapterTitle=${it.title}")

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
                                            color = if(historyViewModel.existHistory(mangaChapterUrl = it.chapterUrl!!)) Color.Black.copy(alpha = 0.6f) else Color.Black,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth(0.7f)
                                        )

                                        Row {
                                            Icon(
                                                painter = painterResource(id = R.drawable.outline_eye),
                                                contentDescription = "View icon",
                                                tint = Color.Black.copy(alpha = 0.6f),
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = it.view ?: "??",
                                                color = Color.Black.copy(alpha = 0.6f),
                                                fontSize = 13.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Text(
                                        text = it.uploadedAt ?: "",
                                        color = Color.Black.copy(alpha = 0.6f),
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .fillMaxWidth(0.4f)
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color.Black)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                )

                            }
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateRightPadding(LayoutDirection.Ltr),
                        top = paddingValues.calculateTopPadding(),
                        bottom = 340.dp
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).addHeader("Referer", "https://www.nelomanga.com/").data(mangaDetail.thumbnail).build(),
                        contentDescription = "manga cover",
                        loading = { CircularProgressIndicator()},
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(145.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    )

                    AutoResizableText(
                        customText = mangaDetail.title ?: "Unknown",
                        customFontSize = 22.sp,
                        customFontWeight = FontWeight.SemiBold,
                        themeTextColor = Color.Black,
                        customMaxLines = 2
                    )
                    AutoResizableText(
                        customText = if(authors.isEmpty()) "Updating..." else authors.joinToString(" , "),
                        customFontSize = 14.sp,
                        customFontWeight = FontWeight.Normal,
                        themeTextColor = Color.Black.copy(alpha = 0.6f),
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_star),
                                contentDescription = "rating icon",
                                tint = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(22.dp)
                            )
                            Text(
                                text = mangaDetail.rating ?: "0.0",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_eye),
                                contentDescription = "view icon",
                                tint = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(22.dp)
                            )
                            Text(
                                text = mangaDetail.view ?: "??",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.status),
                                contentDescription = "rating icon",
                                tint = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(20.dp)
                            )
                            Text(
                                text = mangaDetail.status ?: "Unknown",
                                fontWeight = FontWeight.Normal,
                                color = Color.Black.copy(alpha = 0.6f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(mangaDetail.genres) {
                        Text(
                            text = it.name ?: "",
                            color = Color.Black,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
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

                                    mainViewModel.advancedSearchUrl.value = it.url ?: ""
                                    mainViewModel.getAdvancedSearchFeed()
                                }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Black)
                )

                ExtendableText(
                    description = mangaDetail.description ?: "",
                    themeTextColor = Color.Black,
                    themeBackgroundColor = Color.White
                )
            }
        }
    }
}