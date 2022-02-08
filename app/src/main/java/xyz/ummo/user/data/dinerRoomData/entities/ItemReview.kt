package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity
data class ItemReview(
    @PrimaryKey(autoGenerate = false)
    val itemReviewId: String,

    val dateReviewed: DateTime,

    val reviewComment: String,

    val menuItemId: String, //FK

    val rating: Int
)