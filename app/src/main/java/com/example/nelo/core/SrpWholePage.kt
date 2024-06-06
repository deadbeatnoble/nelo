package com.example.nelo.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun srpWholePage(
    baseUrl: String,
    responseListener: ResponseListener
) {
    try {
        val doc = Jsoup.connect(baseUrl).get()

        responseListener.onSuccess(doc = doc)
    } catch (e: Exception) {
        responseListener.onFailure(message = "Unable to ACCESS the page from $baseUrl: $e")
    }
}

interface ResponseListener {
    fun onSuccess(doc: Document)
    fun onFailure(message: String)
}