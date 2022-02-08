package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Cart
import xyz.ummo.user.data.dinerRoomData.entities.ItemReview
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem

data class MenuItemWithItemReviews(
    @Embedded val menuItem: MenuItem,

    @Relation(
            parentColumn = "menuItemId",
            entityColumn = "menuItemId"
    )

    val itemReviews: List<ItemReview>
)