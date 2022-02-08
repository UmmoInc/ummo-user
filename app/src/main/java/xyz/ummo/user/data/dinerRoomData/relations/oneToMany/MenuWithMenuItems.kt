package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem

data class MenuWithMenuItems(
    @Embedded val menu: Menu,

    @Relation(
            parentColumn = "menuId",
            entityColumn = "menuId"
    )

    val menuItems: List<MenuItem>
)