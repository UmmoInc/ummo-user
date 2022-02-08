package xyz.ummo.user.data.dinerRoomData.relations.oneToOne

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.RetailOutlet

/** This Helper Class is a One-to-One relation b/n [RetailOutlet] && [Menu]
 * where the join occurs on [retailOutlet] Id **/

data class RetailOutletAndMenu(
    @Embedded val retailOutlet: RetailOutlet,

    @Relation(
        parentColumn = "retailOutletId",
        entityColumn = "retailOutletId"
    )
    val menu: Menu
)