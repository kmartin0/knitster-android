package nl.kmartin.knitster.data.model

data class RowCounter(
    val id: Long = 0,
    val name: String = "",
    val count: Int = 0,
    val target: Int? = null,
    val position: Int = 0
)