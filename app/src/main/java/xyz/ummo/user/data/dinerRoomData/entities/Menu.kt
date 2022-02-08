package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Menu(
    @PrimaryKey(autoGenerate = false)
    val menuId: String,

    val menuName: String,

    val itemId: String,

    val retailOutletId: String
)