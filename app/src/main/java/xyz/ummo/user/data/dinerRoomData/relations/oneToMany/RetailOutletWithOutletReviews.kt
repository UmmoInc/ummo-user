package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.OutletReview
import xyz.ummo.user.data.dinerRoomData.entities.RetailOutlet

data class RetailOutletWithOutletReviews(
    @Embedded val retailOutlet: RetailOutlet,

    @Relation(
            parentColumn = "retailOutletId",
            entityColumn = "retailOutletId"
    )

    val outletReviews: List<OutletReview>
)