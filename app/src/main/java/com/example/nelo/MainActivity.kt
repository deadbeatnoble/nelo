package com.example.nelo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.nelo.core.FeedResponseListener
import com.example.nelo.core.ResponseListener
import com.example.nelo.core.srpFeed
import com.example.nelo.core.srpWholePage
import com.example.nelo.model.FeedResponseModel
import com.example.nelo.model.MangaModel
import com.example.nelo.screen.MainScreen
import com.example.nelo.screen.browse.BrowseScreen
import com.example.nelo.ui.theme.NeloTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.nodes.Document

class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeloTheme {
                val navController = rememberNavController()
                val mainViewModel = MainViewModel()

                MainScreen(
                    mainViewModel = mainViewModel,
                    navController = navController
                )
            }
        }
    }
}