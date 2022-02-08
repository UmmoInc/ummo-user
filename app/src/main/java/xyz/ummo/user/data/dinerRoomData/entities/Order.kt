package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity
data class Order(
    @PrimaryKey(autoGenerate = false)
    val orderId: String,

    val orderStatus: String,

    val paymentId: String, //FK

    val deliveryInstruction: String,

    val totalOrderPrice: Float,

    val orderReceipt: String,

    val cartId: String, //FK

    val createdAt: DateTime
)