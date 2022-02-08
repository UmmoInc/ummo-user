package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Cart
import xyz.ummo.user.data.dinerRoomData.entities.FavouriteItems
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem

data class FavouriteItemsWithMenuItems(
    @Embedded val favouriteItems: FavouriteItems,

    @Relation(
            parentColumn = "menuItemId",
            entityColumn = "menuItemId"
    )

    val menuItems: List<MenuItem>
)