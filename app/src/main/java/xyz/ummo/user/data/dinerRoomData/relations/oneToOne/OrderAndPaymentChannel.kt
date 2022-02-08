package xyz.ummo.user.data.dinerRoomData.relations.oneToOne

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

/** This Helper Class is a One-to-One relation b/n [Cart] && [Order]
 * where the join occurs on [cart] Id **/

data class OrderAndPaymentChannel(
    @Embedded val order: Order,

    @Relation(
        parentColumn = "paymentId",
        entityColumn = "paymentId"
    )
    val paymentChannel: PaymentChannel
)