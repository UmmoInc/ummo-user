package xyz.ummo.user.data.dinerRoomData.relations.oneToMany

import androidx.room.Embedded
import androidx.room.Relation
import xyz.ummo.user.data.dinerRoomData.entities.*

data class ProfileWithPaymentChannels(
    @Embedded val profile: Profile,

    @Relation(
            parentColumn = "paymentId",
            entityColumn = "paymentId"
    )

    val paymentChannels: List<PaymentChannel>
)