package com.mad.susach.data.wikipedia.remote

import com.google.gson.annotations.SerializedName

// For action=query&prop=extracts (primary method for HTML extract)
data class MediaWikiResponse(
    val batchcomplete: String?,
    val query: WikiQuery?
)

data class WikiQuery(
    val normalized: List<NormalizedTitle>?,
    val redirects: List<RedirectedTitle>?,
    val pages: Map<String, WikiPage>?,
)

data class NormalizedTitle(
    val from: String?,
    val to: String?
)

data class RedirectedTitle(
    val from: String?,
    val to: String?
)

data class WikiPage(
    val pageid: Int?,
    val ns: Int?,
    val title: String?,
    val extract: String?, // This will hold the HTML extract
    @SerializedName("missing") val missing: String?
)

// For action=parse&prop=text (fallback method for full HTML)
data class MediaWikiParseResponse(
    val parse: WikiParseResult?
)

data class WikiParseResult(
    val title: String?,
    val pageid: Int?,
    // The 'text' field will hold the full HTML content from action=parse&prop=text
    val text: WikiContent?,
    // The 'wikitext' field is no longer the primary target, but kept for schema completeness if API ever sends it unexpectedly
    val wikitext: WikiContent?
)

// Represents the content object (e.g., {"*": "..."}) for HTML from parse API or extracts.
data class WikiContent(
    @SerializedName("*") val content: String?
)
