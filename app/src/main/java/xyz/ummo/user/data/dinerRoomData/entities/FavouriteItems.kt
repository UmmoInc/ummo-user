package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity
data class FavouriteItems(
    @PrimaryKey(autoGenerate = false)
    val menuItemId: String, //FK

    val itemName: String,

    val dateFavoured: DateTime,

    val dateRemoved: DateTime
)