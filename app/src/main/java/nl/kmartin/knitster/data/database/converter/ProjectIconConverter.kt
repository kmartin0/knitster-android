package nl.kmartin.knitster.data.database.converter

import androidx.room.TypeConverter
import nl.kmartin.knitster.data.model.ProjectIcon

class ProjectIconConverter {
    @TypeConverter
    fun fromProjectIcon(icon: ProjectIcon): String {
        return icon.id
    }

    @TypeConverter
    fun toProjectIcon(id: String): ProjectIcon {
        return ProjectIcon.fromId(id)
    }
}