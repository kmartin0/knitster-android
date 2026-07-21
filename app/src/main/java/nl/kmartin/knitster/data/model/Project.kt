package nl.kmartin.knitster.data.model

data class Project(
    val id: Long = 0,
    val name: String,
    val icon: ProjectIcon,
    val notes: String = "",
    val rowCount: Int = 0,
    val lastSavedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)