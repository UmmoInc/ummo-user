package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity
data class OutletReview(
    @PrimaryKey(autoGenerate = false)
    val outletReviewId: String,

    val reviewComment: String,

    val dateReviewed: DateTime,

    val retailOutletId: String, //FK

    val rating: String
)