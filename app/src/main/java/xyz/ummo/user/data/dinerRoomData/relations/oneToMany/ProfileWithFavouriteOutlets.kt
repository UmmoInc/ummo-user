package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

data class ProfileWithFavouriteOutlets(
    @Embedded val profile: Profile,

    @Relation(
            parentColumn = "favouriteOutletId",
            entityColumn = "retailOutletId"
    )

    val favouriteOutlets: List<FavouriteOutlets>
)