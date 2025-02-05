package com.example.nelo.presentation.screen.chapterview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.data.model.HistoryEntity
import com.example.nelo.presentation.viewmodels.HistoryViewModel
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.viewmodels.SharedViewModel
import com.example.nelo.util.UiState

@Composable
fun PageListView(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    chapterUrl: String,
    chapterTitle: String,
    sharedViewModel: SharedViewModel,
    historyViewModel: HistoryViewModel
) {
    val uiState by sharedViewModel.chapterDetailsUiState.collectAsState()
    val prevChapterDetail by sharedViewModel.prevChapterDetails.collectAsState()
    val nextChapterDetail by sharedViewModel.nextChapterDetails.collectAsState()

    val state = rememberLazyListState()

    LaunchedEffect(true) {
        Log.e("TEXT_PAGE_UI", "loading chapter, fetching...")
        sharedViewModel.getChapterDetails(chapterUrl = chapterUrl, chapterTitle = chapterTitle)
        Log.e("TEXT_PAGE_UI", "loading chapter, fecthed")
        state.scrollToItem(0)
    }

    val toggleTheme = mainViewModel.toggleTheme.value
    val manga = mainViewModel.mangaDetail.value

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White


    when(uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }
        is UiState.Success -> {
            val chapterDetail = (uiState as UiState.Success<ChapterModel>).data
            //addToHistory()
            historyViewModel.addHistory(
                historyEntity = HistoryEntity(
                    id = 0,
                    mangaTitle = manga.title!!,
                    mangaUrl = manga.mangaUrl!!,
                    mangaThumbnail = manga.thumbnail!!,
                    mangaChapter = chapterDetail.title!!,
                    mangaChapterUrl = chapterDetail.chapterUrl!!
                )
            )

            LazyColumn(
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeBackgroundColor)
            ) {
                item {
                    NeloPageListViewTop(
                        mangaDetail = manga,
                        currentChapter = chapterDetail,
                        onBackClicked = {
                                        navController.popBackStack()
                        },
                        onChapterSelected = { chapter ->
                            sharedViewModel.getChapterDetails(chapter.chapterUrl!!, chapter.title!!)
                        }
                    )
                }

                if (chapterDetail.pages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Fetching all pages...",
                                color = themeTextColor
                            )
                        }
                    }
                } else {
                    items(chapterDetail.pages) {
                        SubcomposeAsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .addHeader("Referer", chapterDetail.chapterUrl)
                                .data(it.pageImageUrl!!)
                                .build(),
                            contentDescription = "page image",
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(600.dp)
                                        .padding(bottom = 8.dp)
                                        .background(Color.LightGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(600.dp)
                                        .padding(bottom = 8.dp)
                                        .background(Color.LightGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(
                                        onClick = {

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Red
                                        )
                                    ) {
                                        Text(
                                            text = "Retry",
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    NeloPageListViewBottom(
                        previousChapter = prevChapterDetail,
                        currentChapter = chapterDetail,
                        nextChapterModel = nextChapterDetail,
                        onPreviousClicked = {
                            if(prevChapterDetail.chapterUrl != null) {
                                sharedViewModel.getChapterDetails(prevChapterDetail.chapterUrl!!, prevChapterDetail.title!!)
                            }
                        },
                        onNextClicked = {
                            if(nextChapterDetail.chapterUrl != null) {sharedViewModel.getChapterDetails(nextChapterDetail.chapterUrl!!, nextChapterDetail.title!!)}
                        }
                    )
                }
            }
        }
        is UiState.Error -> {
            Text(text = (uiState as UiState.Error).message)
        }
    }
}

@Composable
fun NeloPageListViewTop(
    mangaDetail: MangaModel,
    currentChapter: ChapterModel,
    onBackClicked: () -> Unit,
    onChapterSelected: (ChapterModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility
    // Top bar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = {
                      onBackClicked.invoke()
            },
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

        Spacer(modifier = Modifier.width(8.dp))

        // Chapter Title Dropdown Button
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(48.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(48.dp))
                    .background(colorResource(id = R.color.orange_bg))
                    .clickable { expanded = true }
                    .padding(12.dp)
            ) {
                Text(
                    text = currentChapter.title ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", modifier = Modifier.clickable { expanded = true })
            }

            // Dropdown Menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Adjust width
                    .heightIn(max = 250.dp)
                    .background(Color.White)
                    //.verticalScroll(scrollState) // Enable scrolling if needed
            ) {
                mangaDetail.chapterList.forEach { chapter ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            //onChapterSelect(chapter)
                        }
                    ) {
                        Text(
                            text = chapter.title ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                    onChapterSelected.invoke(chapter)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeloPageListViewBottom(
    previousChapter: ChapterModel,
    currentChapter: ChapterModel,
    nextChapterModel: ChapterModel,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(48.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 48.dp, bottomStart = 48.dp))
                .border(
                    2.dp,
                    Color.Black,
                    RoundedCornerShape(topStart = 48.dp, bottomStart = 48.dp)
                )
                .background(
                    if (previousChapter.title.isNullOrEmpty()) Color.LightGray else colorResource(
                        id = R.color.blue_bg
                    )
                )
                .padding(8.dp)
                .clickable { onPreviousClicked.invoke() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mini_left_arrow),
                contentDescription = "previous icon",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .border(2.dp, Color.Black)
        ) {
            Text(
                text = currentChapter.title ?: "",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topEnd = 48.dp, bottomEnd = 48.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(topEnd = 48.dp, bottomEnd = 48.dp))
                .background(
                    if (nextChapterModel.title.isNullOrEmpty()) Color.LightGray else colorResource(
                        id = R.color.blue_bg
                    )
                )
                .padding(8.dp)
                .clickable { onNextClicked.invoke() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mini_right_arrow),
                contentDescription = "previous icon",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}