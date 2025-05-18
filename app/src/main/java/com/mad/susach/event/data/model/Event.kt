
data class Event(
    val id: String,
    val eraId: String,
    val name: String,
    val description: String,
    val time: String,
    val imageURL: String,
    val imageContent: String,
    val summary: String,
    var content: String
)
