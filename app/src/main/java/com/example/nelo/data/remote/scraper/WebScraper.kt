package com.example.nelo.data.remote.scraper

import com.example.nelo.util.CustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebScraper {
    suspend fun scrapeWebPage(url: String): Result<Document> {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()
                Result.success(doc)
            } catch (e: Exception) {
                Result.failure(CustomError("Unable to ACCESS the page from $url: $e"))
            }
        }
    }
}