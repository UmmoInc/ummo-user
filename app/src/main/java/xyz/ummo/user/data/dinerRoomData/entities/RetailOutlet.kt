package xyz.ummo.user.data.dinerRoomData.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity
data class RetailOutlet(
    @PrimaryKey(autoGenerate = false)
    val retailOutletId: String,

    val retailOutletName: String,

    val description: String,

    val location: String,

    val rating: Float,

    val openTime: Time,

    val closeTime: Time,

    val retailImage: Uri,

    val retailType: String,

    val deliverable: Boolean,

    val menuId: String
)