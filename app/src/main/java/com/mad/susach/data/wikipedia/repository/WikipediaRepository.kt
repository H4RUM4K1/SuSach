package com.mad.susach.data.wikipedia.repository

import android.text.Html // For decoding HTML entities in titles if needed
import android.util.Log // For logging
import com.mad.susach.data.wikipedia.remote.MediaWikiApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Added import for HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WikipediaRepository @Inject constructor() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private fun getMediaWikiApiService(languageCode: String, useRestApi: Boolean = false): MediaWikiApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Changed to BODY for better debugging, consider NONE for release
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val baseUrl = if (useRestApi) {
            "https://$languageCode.wikipedia.org/api/rest_v1/"
        } else {
            "https://$languageCode.wikipedia.org/w/"
        }
        Log.d("WikipediaRepository", "Using base URL: $baseUrl")

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            // Add ScalarConverterFactory for plain text/html responses BEFORE GsonConverterFactory
            .addConverterFactory(retrofit2.converter.scalars.ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MediaWikiApiService::class.java)
    }

    suspend fun fetchWikipediaArticleHtml(pageTitleFromUrl: String, languageCode: String): String? {
        Log.d("WikipediaRepository", "Attempting to fetch mobile HTML for title: '$pageTitleFromUrl', lang: '$languageCode' using /page/mobile-html endpoint")
        return withContext(ioDispatcher) {
            try {
                // Use the REST API for /page/mobile-html
                val apiService = getMediaWikiApiService(languageCode, useRestApi = true)
                val response = apiService.getMobileHtml(title = pageTitleFromUrl)

                if (response.isSuccessful) {
                    val htmlContent: String? = response.body()
                    if (!htmlContent.isNullOrBlank()) {
                        Log.d("WikipediaRepository", "Successfully fetched mobile HTML for '$pageTitleFromUrl'. Length: ${htmlContent.length}")
                        // Basic CSS for mobile-friendliness - can be expanded
                        val styledHtml = """
                            <style>
                                body { font-family: sans-serif; margin: 0; padding: 0; background-color: #f9f9f9; color: #333; }
                                img { max-width: 100%; height: auto; display: block; margin-left: auto; margin-right: auto; }
                                a { color: #0645ad; text-decoration: none; }
                                h1, h2, h3, h4, h5, h6 { color: #000; line-height: 1.2; margin-top: 1em; margin-bottom: 0.5em; }
                                .content { line-height: 1.6; }
                                .infobox { background-color: #f0f0f0; border: 1px solid #aaa; padding: 0.5em; margin: 1em 0; float: right; clear: right; max-width: 300px; font-size: 0.9em; }
                                .infobox th { background-color: #e0e0e0; text-align: center; }
                                .infobox td { padding: 0.2em; }
                                table.wikitable { border-collapse: collapse; margin: 1em 0; border: 1px solid #aaa; }
                                table.wikitable th, table.wikitable td { border: 1px solid #aaa; padding: 0.35em; }
                                table.wikitable th { background-color: #f2f2f2; text-align: center; }
                                .gallery { display: flex; flex-wrap: wrap; justify-content: space-around; }
                                .gallerybox { margin: 0.5em; border: 1px solid #ccc; padding: 0.5em; text-align: center; width: 150px; }
                                .gallerybox img { margin-bottom: 0.5em; }
                                /* Add more styles as needed, e.g., for tables, infoboxes, etc. */
                            </style>
                            <div class="content" style="margin: 0; padding: 0;">
                                $htmlContent
                            </div>
                        """.trimIndent()
                        styledHtml
                    } else {
                        Log.w("WikipediaRepository", "Mobile HTML response body was null or blank for '$pageTitleFromUrl'")
                        null
                    }
                } else {
                    val errorBody = response.errorBody()?.string() // Read error body only once
                    Log.e("WikipediaRepository", "Mobile HTML API Error for '$pageTitleFromUrl': ${response.code()} - ${response.message()}. Error Body: $errorBody")
                    // Fallback or further error handling can be added here if needed
                    null
                }
            } catch (e: Exception) {
                Log.e("WikipediaRepository", "Exception fetching mobile HTML for '$pageTitleFromUrl'", e)
                null
            }
        }
    }
}
