package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.FavouriteOutlets
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.MenuItem
import xyz.ummo.user.data.dinerRoomData.entities.RetailOutlet

data class FavouriteOutletsWithRetailOutlets(
    @Embedded val favouriteOutlets: FavouriteOutlets,

    @Relation(
            parentColumn = "retailOutletId",
            entityColumn = "retailOutletId"
    )

    val retailOutlets: List<RetailOutlet>
)