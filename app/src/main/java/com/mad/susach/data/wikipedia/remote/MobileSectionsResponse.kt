package com.mad.susach.data.wikipedia.remote

import com.google.gson.annotations.SerializedName

// Main response structure for /page/mobile-sections/{title}
data class MobileSectionsResponse(
    @SerializedName("lead") val lead: LeadSection?,
    @SerializedName("remaining") val remaining: RemainingSections?
)

data class LeadSection(
    @SerializedName("id") val id: Int?,
    @SerializedName("revision") val revision: Long?,
    @SerializedName("lastmodified") val lastmodified: String?,
    @SerializedName("displaytitle") val displaytitle: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_source") val descriptionSource: String?,
    @SerializedName("protection") val protection: Map<String, List<String>>?,
    @SerializedName("editable") val editable: Boolean?,
    @SerializedName("sections") val sections: List<SectionContentItem>?
)

data class RemainingSections(
    @SerializedName("sections") val sections: List<SectionContentItem>?
)

// Represents a single content section (can be part of lead or remaining)
data class SectionContentItem(
    @SerializedName("id") val id: Int,
    @SerializedName("toclevel") val tocLevel: Int?,
    @SerializedName("level") val level: String?,
    @SerializedName("line") val line: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("index") val index: String?,
    @SerializedName("fromtitle") val fromTitle: String?,
    @SerializedName("anchor") val anchor: String?,
    @SerializedName("text") val text: String?
)
