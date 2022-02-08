package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity
data class FavouriteOutlets(
    @PrimaryKey(autoGenerate = false)
    val retailOutletId: String, //FK

    val dateFavoured: DateTime,

    val dateRemoved: DateTime
)