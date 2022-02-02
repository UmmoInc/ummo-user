package xyz.ummo.user.rvItems

import android.content.Context
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.food_menu_card.view.*
import xyz.ummo.user.R
import xyz.ummo.user.models.FoodMenuModel
import xyz.ummo.user.ui.fragments.diner.foodCourt.menu.item.FoodItemInfoBottomSheet
import xyz.ummo.user.ui.main.MainScreen.Companion.supportFM
import xyz.ummo.user.utilities.FOOD_ITEM_OBJECT

class FoodMenuItem(
    private val foodMenuModel: FoodMenuModel,
    val context: Context
) : Item<GroupieViewHolder>() {

    private lateinit var picasso: Picasso

    override fun getLayout(): Int {
        return R.layout.food_menu_card
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        /** Setting up [Picasso] **/
        picasso = Picasso.get()

        viewHolder.itemView.food_item_title_text_view.text = foodMenuModel.menuItemName
        viewHolder.itemView.food_item_description_text_view.text = foodMenuModel.menuItemDescription
        viewHolder.itemView.food_item_image_view.setImageDrawable(foodMenuModel.menuItemImage)
//        picasso.load(foodMenuModel.menuItemImage).into(viewHolder.itemView.food_item_image_view)
        viewHolder.itemView.food_item_price_range_text_view.text = foodMenuModel.menuItemPriceRange

        /** Handling all click instances on the card **/
        viewHolder.itemView.food_item_image_view.setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.food_item_title_text_view.setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.food_item_description_text_view.setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.food_item_price_range_text_view.setOnClickListener { launchFoodItemOrderBottomSheet() }
        viewHolder.itemView.add_to_cart_button.setOnClickListener { launchFoodItemOrderBottomSheet() }
    }

    private fun launchFoodItemOrderBottomSheet() {
        val foodItemBundle = Bundle()
        foodItemBundle.putSerializable(FOOD_ITEM_OBJECT, foodMenuModel)
        val foodItemOrderBottomSheet = FoodItemInfoBottomSheet()
        foodItemOrderBottomSheet.show(supportFM, FoodItemInfoBottomSheet.TAG)
    }
}