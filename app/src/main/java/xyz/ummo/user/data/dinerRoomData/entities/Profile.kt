package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = false)
    val contact: String,

    val fullName: String,

    val email: String,

    val verified: Boolean,

    val paymentId: String, //FK

    val favouriteItemId: String, //FK

    val itemReviewId: String, //FK

    val outletReviewId: String, //FK

    val favouriteOutletId: String //FK
)