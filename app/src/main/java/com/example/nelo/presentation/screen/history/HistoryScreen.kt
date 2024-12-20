package com.example.nelo.presentation.screen.history

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.data.history.HistoryViewModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.navigation.DetailNavScreens
import com.example.nelo.presentation.navigation.RootNavGraphs

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    val mHistoryViewModel: HistoryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(
        HistoryViewModel::class.java)

    val toggleTheme = mainViewModel.toggleTheme.value
    val history by mHistoryViewModel.mangaHistory.collectAsState()

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White
    val optionsExpanded = remember {
        mutableStateOf(false)
    }

    //mHistoryViewModel.deleteHistory(mangaChapterUrl = it.mangaChapterUrl)

    LaunchedEffect(true) {
        mHistoryViewModel.getHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "History",
                        color = themeBackgroundColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { optionsExpanded.value = !optionsExpanded.value }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.detail),
                            contentDescription = "more option",
                            tint = themeBackgroundColor,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = optionsExpanded.value,
                        onDismissRequest = {
                            optionsExpanded.value = false
                        },
                        modifier = Modifier
                            .background(themeBackgroundColor)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                mHistoryViewModel.clearHistory()
                                optionsExpanded.value = false
                            }
                        ) {
                            Text(
                                text = "Clear History",
                                color = themeTextColor
                            )
                        }
                    }
                },
                backgroundColor = themeTextColor,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        containerColor = themeBackgroundColor,
        content = {
            LazyColumn(
                contentPadding = PaddingValues(top = 55.dp, bottom = 50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(history) {
                    val isContextMenuVisible = remember {
                        mutableStateOf(false)
                    }
                    val pressOffset = remember {
                        mutableStateOf(DpOffset.Zero)
                    }
                    val itemHeight = remember {
                        mutableStateOf(0.dp)
                    }
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val density = LocalDensity.current

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .onSizeChanged {
                                itemHeight.value = with(density) { it.height.toDp() }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            AsyncImage(
                                model = it.mangaThumbnail,
                                contentDescription = "thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(55.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .indication(interactionSource, LocalIndication.current)
                                    .pointerInput(true) {
                                        detectTapGestures(
                                            onPress = {
                                                val press = PressInteraction.Press(it)
                                                interactionSource.emit(press)
                                                tryAwaitRelease()
                                                interactionSource.emit(PressInteraction.Release(press))
                                            },
                                            onTap = { offset ->
                                                if(navController.currentBackStackEntry?.destination?.route != DetailNavScreens.MangaScreen.route) {
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
                                                    mainViewModel._chapterDetail.value = ChapterModel(
                                                        title = null,
                                                        view = null,
                                                        uploadedAt = null,
                                                        chapterUrl = null,
                                                        pages = emptyList()
                                                    )
                                                    mainViewModel.getManga()
                                                    navController.navigate(RootNavGraphs.DetailGraph.route)
                                                }
                                            }
                                        )
                                    }
                            )
                            Column(
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                                    .indication(interactionSource, LocalIndication.current)
                                    .pointerInput(true) {
                                        detectTapGestures(
                                            onLongPress = {
                                                isContextMenuVisible.value = true
                                                pressOffset.value = DpOffset(it.x.toDp(), it.y.toDp())
                                            },
                                            onPress = {
                                                val press = PressInteraction.Press(it)
                                                interactionSource.emit(press)
                                                tryAwaitRelease()
                                                interactionSource.emit(PressInteraction.Release(press))
                                            },
                                            onTap = { offset ->
                                                mainViewModel._mangaDetail.value = MangaModel(
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
                                                mainViewModel._chapterDetail.value = ChapterModel(
                                                    title = it.mangaChapter,
                                                    view = null,
                                                    uploadedAt = null,
                                                    chapterUrl = it.mangaChapterUrl,
                                                    pages = emptyList()
                                                )
                                                mainViewModel.getManga()
                                                navController.navigate(DetailNavScreens.ChapterScreen.route)
                                            }
                                        )
                                    }
                            ) {
                                Text(
                                    text = it.mangaTitle,
                                    color = themeTextColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )

                                Text(
                                    text = it.mangaChapter,
                                    color = themeTextColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = isContextMenuVisible.value,
                            onDismissRequest = {
                                isContextMenuVisible.value = false
                            },
                            offset = pressOffset.value.copy(
                                y = pressOffset.value.y - itemHeight.value
                            ),
                            modifier = Modifier
                                .background(themeBackgroundColor)
                        ) {
                            DropdownMenuItem(onClick = {
                                mHistoryViewModel.deleteHistory(mangaChapterUrl = it.mangaChapterUrl)
                                isContextMenuVisible.value = false
                            }) {
                                Text(
                                    text = "Delete history",
                                    color = themeTextColor
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(mainViewModel = MainViewModel(), navController = rememberNavController())
}