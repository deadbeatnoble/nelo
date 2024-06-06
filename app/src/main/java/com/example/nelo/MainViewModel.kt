package com.example.nelo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.nelo.core.ChapterResponseListener
import com.example.nelo.core.FeedResponseListener
import com.example.nelo.core.MangaResponseListener
import com.example.nelo.core.ResponseListener
import com.example.nelo.core.srpAdvancedSearch
import com.example.nelo.core.srpChapter
import com.example.nelo.core.srpFeed
import com.example.nelo.core.srpMangaDetail
import com.example.nelo.core.srpWholePage
import com.example.nelo.model.ChapterModel
import com.example.nelo.model.ChapterResponseModel
import com.example.nelo.model.FeedResponseModel
import com.example.nelo.model.MangaModel
import com.example.nelo.model.PageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.nodes.Document

class MainViewModel: ViewModel() {

    var onUpdate = mutableStateOf(0)

    fun updateUI() {
        onUpdate.value = (0..1_000_000).random()
    }

    var keyword = mutableStateOf("")
    var selectedKeyword = mutableStateOf("Everything")

    var _selectedTab = mutableStateOf("Popular")
    val selectedTab = _selectedTab
    var feedChange = mutableStateOf(false)
    fun updateSelectedTab(newValue: String) {
        if (_selectedTab.value != newValue) {
            _selectedTab.value = newValue
            _feedResponse.value.clear()
            feedChange.value = true
            _hasNextPage.value = true
            _currentPage.value = 1
            getFeed()
        }
    }

    var _toggleTheme = mutableStateOf(true)
    val toggleTheme = _toggleTheme

    var _toggleFavorite = mutableStateOf(true)
    val toggleFavorite = _toggleFavorite


    @SuppressLint("MutableCollectionMutableState")
    var _feedResponse = mutableStateOf(mutableListOf<MangaModel>())
    val feedResponse = _feedResponse

    private var _feedResponseError = mutableStateOf("")
    val feedResponseError = _feedResponseError

    private var _isLoading = mutableStateOf(false)
    val isLoading = _isLoading

    var _hasNextPage = mutableStateOf(true)
    var _currentPage = mutableStateOf(1)

    fun getFeed() {
        _isLoading.value = true
        val page = if(_currentPage.value == 1) null else _currentPage.value

        if (_hasNextPage.value){
            CoroutineScope(Dispatchers.IO).launch {
                srpWholePage(
                    baseUrl = getUrl(
                        type = _selectedTab.value,
                        page = page
                    ),
                    responseListener = object : ResponseListener {
                        override fun onSuccess(doc: Document) {
                            srpFeed(
                                doc = doc,
                                feedResponseListener = object : FeedResponseListener {
                                    override fun onSuccess(data: FeedResponseModel) {
                                        if (feedChange.value) {
                                            _feedResponse.value.clear()
                                            _feedResponse.value = data.data.toMutableList()
                                            _feedResponseError.value = ""
                                            feedChange.value = false
                                        } else {
                                            data.data.forEach {
                                                _feedResponse.value += it
                                            }
                                        }
                                        _hasNextPage.value = data.hasNextPage
                                        _isLoading.value = false

                                        updateUI()
                                    }

                                    override fun onFailure(message: String) {
                                        if (page != 1) _currentPage.value--
                                        _feedResponseError.value = message
                                        _isLoading.value = false
                                    }

                                }
                            )
                        }

                        override fun onFailure(message: String) {
                            if (page != 1) _currentPage.value--
                            _feedResponseError.value = message
                            _isLoading.value = false
                        }

                    }
                )
            }

            _currentPage.value++
        } else {
            _isLoading.value = false
        }
    }

    var advancedSearchUrl = mutableStateOf("")
    fun getAdvancedSearchFeed() {
        _isLoading.value = true
        if (_hasNextPage.value) {
            CoroutineScope(Dispatchers.IO).launch {
                srpWholePage(
                    baseUrl = advancedSearchUrl.value,
                    responseListener = object: ResponseListener {
                        override fun onSuccess(doc: Document) {
                            srpAdvancedSearch(
                                doc = doc,
                                feedResponseListener = object: FeedResponseListener {
                                    override fun onSuccess(data: FeedResponseModel) {
                                        if (feedChange.value) {
                                            _feedResponse.value = data.data.toMutableList()
                                            _feedResponseError.value = ""
                                            feedChange.value = false
                                        } else {
                                            data.data.forEach {
                                                _feedResponse.value += it
                                            }
                                        }
                                        _hasNextPage.value = data.hasNextPage
                                        _isLoading.value = false
                                        _currentPage.value++

                                        updateUI()
                                    }

                                    override fun onFailure(message: String) {
                                        _feedResponseError.value = message
                                        _isLoading.value = false
                                    }

                                }
                            )
                        }

                        override fun onFailure(message: String) {
                            _feedResponseError.value = message
                            _isLoading.value = false
                        }

                    }
                )
            }
        } else {
            _isLoading.value = false
        }
    }

    var _mangaDetail = mutableStateOf(
        value = MangaModel(
            title = null,
            thumbnail = null,
            authors = emptyList(),
            genres = emptyList(),
            status = null,
            updatedAt = null,
            description = null,
            view = null,
            rating = null,
            mangaUrl = null,
            chapterList = emptyList()
        )
    )
    val mangaDetail = _mangaDetail

    private var _mangaDetailError = mutableStateOf("")
    val mangaDetailError = _mangaDetailError

    private var _mangaDetailLoading = mutableStateOf(false)
    val mangaDetailLoading = _mangaDetailLoading

    fun getManga() {
        val url = _mangaDetail.value.mangaUrl

        _mangaDetailLoading.value = true
        if (!url.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                srpWholePage(
                    baseUrl = url,
                    responseListener = object: ResponseListener {
                        override fun onSuccess(doc: Document) {
                            srpMangaDetail(
                                doc = doc,
                                mangaUrl = url,
                                mangaResponseListener = object: MangaResponseListener {
                                    override fun onSuccess(data: MangaModel) {
                                        _mangaDetail.value = data
                                        _mangaDetailError.value = ""
                                        _mangaDetailLoading.value = false

                                        if (_chapterDetail.value.chapterUrl != null) {
                                            for (chap in _mangaDetail.value.chapterList) {
                                                if (chap.chapterUrl == _chapterDetail.value.chapterUrl) {
                                                    _chapterDetail.value = chap
                                                    getChapter()
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(message: String) {
                                        _mangaDetailError.value = message
                                        _mangaDetailLoading.value = false
                                    }

                                }
                            )
                        }

                        override fun onFailure(message: String) {
                            _mangaDetailError.value = message
                            _mangaDetailLoading.value = false
                        }

                    }
                )
            }
        } else {
            _mangaDetailError.value = "No URL attached!!!"
            _mangaDetailLoading.value = false
        }
    }

    var _chapterDetail = mutableStateOf(
        value = ChapterModel(
            title = null,
            view = null,
            uploadedAt = null,
            chapterUrl = null,
            pages = emptyList()
        )
    )
    val chapterDetail = _chapterDetail

    var _prevChapterDetail = mutableStateOf(
        value = ChapterModel(
            title = null,
            view = null,
            uploadedAt = null,
            chapterUrl = null,
            pages = emptyList()
        )
    )
    val prevChapterDetail = _prevChapterDetail

    var _nextChapterDetail = mutableStateOf(
        value = ChapterModel(
            title = null,
            view = null,
            uploadedAt = null,
            chapterUrl = null,
            pages = emptyList()
        )
    )
    val nextChapterDetail = _nextChapterDetail

    private var _chapterDetailError = mutableStateOf("")
    val chapterDetailError = _chapterDetailError

    private var _chapterDetailLoading = mutableStateOf(false)
    val chapterDetailLoading = _chapterDetailLoading

    fun getChapter() {

        _chapterDetailLoading.value = true

        val index = _mangaDetail.value.chapterList.indexOf(_chapterDetail.value)

        _prevChapterDetail.value = when(_chapterDetail.value) {
            _mangaDetail.value.chapterList.lastOrNull() -> ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
            else -> _mangaDetail.value.chapterList.get(index + 1)
        }

        _nextChapterDetail.value = when(_chapterDetail.value) {
            _mangaDetail.value.chapterList.firstOrNull() -> ChapterModel(title = null, view = null, uploadedAt = null, chapterUrl = null, pages = emptyList())
            else -> _mangaDetail.value.chapterList.get(index - 1)
        }

        Log.e("TESTING", "prev ->" + _prevChapterDetail.value.toString())
        Log.e("TESTING", "next ->" + _nextChapterDetail.value.toString())

        if (_chapterDetail.value.chapterUrl != null) {
            CoroutineScope(Dispatchers.IO).launch {
                srpWholePage(
                    baseUrl = _chapterDetail.value.chapterUrl!!,
                    responseListener = object : ResponseListener {
                        override fun onSuccess(doc: Document) {
                            srpChapter(
                                doc = doc,
                                referrer = _chapterDetail.value.chapterUrl!!,
                                chapterResponseListener = object : ChapterResponseListener {
                                    override fun onSuccess(data: ChapterResponseModel) {
                                        _chapterDetail.value = ChapterModel(
                                            title = _chapterDetail.value.title,
                                            view = _chapterDetail.value.view,
                                            uploadedAt = _chapterDetail.value.uploadedAt,
                                            chapterUrl = _chapterDetail.value.chapterUrl,
                                            pages = data.data
                                        )

                                        _chapterDetailError.value = ""
                                        _chapterDetailLoading.value = false
                                    }

                                    override fun onFailure(message: String) {
                                        _chapterDetailError.value = message
                                        _chapterDetailLoading.value = false
                                    }

                                }
                            )
                        }

                        override fun onFailure(message: String) {
                            _chapterDetailError.value = message
                            _chapterDetailLoading.value = false
                        }
                    }
                )
            }
        } else {
            _chapterDetailError.value = "No URL attached!!!"
            _chapterDetailLoading.value = false
        }
    }

    private fun getUrl(
        type: String,
        page: Int? = null
    ): String {
        return buildString {
            append("https://manganato.com/genre-all")
            page?.let { append("/$it") }
            when(type) {
                "Popular" -> append("?type=topview")
                "Latest" -> append("")
            }
        }
    }
}