package com.mad.susach.data.wikipedia.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaWikiApiService {
    @GET("api.php")
    suspend fun getPageContent(
        @Query("action") action: String = "query",
        @Query("prop") prop: String = "revisions",
        @Query("rvprop") rvprop: String = "content",
        @Query("rvslots") rvslots: String = "main", // To get content from the main slot
        @Query("format") format: String = "json",
        @Query("titles") titles: String // The title of the Wikipedia page
    ): Response<MediaWikiResponse>

    @GET("api.php")
    suspend fun getPageExtract(
        @Query("action") action: String = "query",
        @Query("prop") prop: String = "extracts",
        @Query("explaintext") explaintext: Boolean = false, // false for HTML
        @Query("format") formatQuery: String = "json", // Renamed to avoid conflict if any default 'format' existed
        @Query("redirects") redirectsQuery: Boolean = true, // Renamed
        @Query("titles") titles: String
    ): Response<MediaWikiResponse> // Uses MediaWikiResponse for prop=extracts

    @GET("api.php")
    suspend fun getParsedPageHtml(
        @Query("action") action: String = "parse",
        @Query("page") pageTitle: String,
        @Query("prop") prop: String = "text", // Changed to "text" for HTML
        @Query("format") formatParse: String = "json", // Renamed
        @Query("redirects") redirectsParse: Boolean = true // Renamed
    ): Response<MediaWikiParseResponse> // Uses MediaWikiParseResponse for action=parse

    // REST API: /page/mobile-sections/{title}
    @GET("page/mobile-sections/{title}")
    suspend fun getMobileSections(
        @Path("title") title: String,
        @Query("redirect") redirect: String = "true"
    ): Response<MobileSectionsResponse>

    @GET("page/mobile-html/{title}")
    suspend fun getMobileHtml(
        @Path("title") title: String,
        @Query("redirect") redirect: String = "true" // Ensure redirects are handled
    ): Response<String> // The response is directly the HTML string
}
