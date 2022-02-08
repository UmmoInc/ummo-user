package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

data class ProfileWithFavouriteItems(
    @Embedded val profile: Profile,

    @Relation(
            parentColumn = "favouriteItemId",
            entityColumn = "menuItemId"
    )

    val favouriteItems: List<FavouriteItems>
)