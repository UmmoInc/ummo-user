package xyz.ummo.user.models

import android.graphics.drawable.Drawable

data class FoodJointModel(
    var foodJointId: String,
    var foodJointName: String,
    var foodJointDescription: String,
    var foodJointImage: Drawable,
    var openingTime: Int,
    var closingTime: Int
)