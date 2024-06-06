package com.example.nelo.screen.chapterview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.nelo.MainViewModel
import com.example.nelo.data.history.HistoryEntity
import com.example.nelo.data.history.HistoryViewModel

@Composable
fun PageListView(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val mHistoryViewModel: HistoryViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!).get(
        HistoryViewModel::class.java)

    val toggleTheme = mainViewModel.toggleTheme.value
    val manga = mainViewModel.mangaDetail.value
    val chapterDetail = mainViewModel.chapterDetail.value
    val prevChapterDetail = mainViewModel.prevChapterDetail.value
    val nextChapterDetail = mainViewModel.nextChapterDetail.value

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White

    if (manga.chapterList.isNotEmpty() && chapterDetail.chapterUrl != null){
        //addToHistory()
        mHistoryViewModel.addHistory(
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
            modifier = Modifier
                .fillMaxSize()
                .background(themeBackgroundColor)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeTextColor)
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "${manga.title} >> ${chapterDetail.title}",
                        color = themeBackgroundColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(prevChapterDetail.chapterUrl != null){
                        Button(
                            onClick = {
                                mainViewModel._chapterDetail.value = prevChapterDetail
                                mainViewModel.getChapter()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Previous",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if(nextChapterDetail.chapterUrl != null){
                        Button(
                            onClick = {
                                mainViewModel._chapterDetail.value = nextChapterDetail
                                mainViewModel.getChapter()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "    Next    ",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
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
                            .addHeader("Referer", chapterDetail.chapterUrl!!)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(prevChapterDetail.chapterUrl != null){
                        Button(
                            onClick = {
                                mainViewModel._chapterDetail.value = prevChapterDetail
                                mainViewModel.getChapter()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Previous",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if(nextChapterDetail.chapterUrl != null){
                        Button(
                            onClick = {
                                mainViewModel._chapterDetail.value = nextChapterDetail
                                mainViewModel.getChapter()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "    Next    ",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeTextColor)
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "${manga.title} >> ${chapterDetail.title}",
                        color = themeBackgroundColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Preview
@Composable
fun PageListViewPreview() {
    PageListView(navController = rememberNavController(), mainViewModel = MainViewModel())
}