package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PaymentChannel(
    @PrimaryKey(autoGenerate = false)
    val paymentId: String,

    val paymentName: String,

    val createdAt: String,

    val balance: Float
)