package xyz.ummo.bite.localdatabase.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(indices = [Index(value = ["phonecontact", "userprofile_id"], unique = true)])
@Parcelize
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val userprofile_id:Int,
    val phonecontact: String,
    val name: String,
     val surname : String,
    val email: String,
    val IsPhoneverified: Boolean,
    val isPhoneSignedIn : Boolean,
     val password :String ,
     val isExpectingOrder : Boolean,

): Parcelable