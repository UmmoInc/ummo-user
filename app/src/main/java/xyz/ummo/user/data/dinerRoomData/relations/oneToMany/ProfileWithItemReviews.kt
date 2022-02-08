package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

data class ProfileWithItemReviews(
    @Embedded val profile: Profile,

    @Relation(
            parentColumn = "itemReviewId",
            entityColumn = "itemReviewId"
    )

    val itemReviews: List<ItemReview>
)