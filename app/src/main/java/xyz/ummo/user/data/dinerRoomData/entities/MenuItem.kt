package xyz.ummo.user.data.dinerRoomData.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.ummo.user.models.diner.Extra
import xyz.ummo.user.models.diner.Ingredient
import xyz.ummo.user.models.diner.Portion
import xyz.ummo.user.models.diner.Review
import java.sql.Time
import kotlin.time.Duration

@Entity
data class MenuItem(
    @PrimaryKey(autoGenerate = false)
    val menuItemId: String,

    val itemName: String,

    val itemDescription: String,

    val ingredients: ArrayList<Ingredient>,

    val price: Float,

    val itemImage: Uri,

    val portions: ArrayList<Portion>,

    val extras: ArrayList<Extra>,

    val prepTime: Duration,

    val rating: Float,

    val reviews: ArrayList<Review>,

    val menuId: String
)