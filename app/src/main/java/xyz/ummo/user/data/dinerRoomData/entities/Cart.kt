package xyz.ummo.user.data.dinerRoomData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import xyz.ummo.user.models.diner.Extra
import xyz.ummo.user.models.diner.Portion

@Entity
data class Cart(
    @PrimaryKey(autoGenerate = false)
    val cartId: String,


    val menuItemId: String, //FK

    val orderId: String, //FK

    val chosenPortion: ArrayList<Portion>,

    val chosenExtras: ArrayList<Extra>,

    val quantity: Int,

    val totalPrice: Float,

    val createdAt: DateTime
)