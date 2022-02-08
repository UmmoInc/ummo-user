package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Cart
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem

data class CartWithMenuItems(
    @Embedded val cart: Cart,

    @Relation(
            parentColumn = "menuItemId",
            entityColumn = "menuItemId"
    )

    val menuItems: List<MenuItem>
)