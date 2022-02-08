package xyz.ummo.user.data.dinerRoomData.relations.oneToOne

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.Cart
import xyz.ummo.user.data.dinerRoomData.entities.Menu
import xyz.ummo.user.data.dinerRoomData.entities.Order
import xyz.ummo.user.data.dinerRoomData.entities.RetailOutlet

/** This Helper Class is a One-to-One relation b/n [Cart] && [Order]
 * where the join occurs on [cart] Id **/

data class CartAndOrder(
    @Embedded val cart: Cart,

    @Relation(
        parentColumn = "cartId",
        entityColumn = "cartId"
    )
    val order: Order
)