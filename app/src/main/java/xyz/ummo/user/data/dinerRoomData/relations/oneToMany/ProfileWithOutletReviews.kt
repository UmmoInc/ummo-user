package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

data class ProfileWithOutletReviews(
    @Embedded val profile: Profile,

    @Relation(
            parentColumn = "outletReviewId",
            entityColumn = "outletReviewId"
    )

    val outletReviews: List<OutletReview>
)