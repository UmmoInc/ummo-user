package xyz.ummo.user.rvItems

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.food_joint_card.view.*
import xyz.ummo.user.R
import xyz.ummo.user.models.FoodJointModel
import xyz.ummo.user.ui.fragments.diner.foodCourt.menu.FoodMenuFragment

class FoodJointItem(
    private val foodJointModel: FoodJointModel,
    val context: Context
) : Item<GroupieViewHolder>() {

    private val bundle = Bundle()
    private lateinit var mixpanelAPI: MixpanelAPI

    override fun getLayout(): Int {
        return R.layout.food_joint_card
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.food_joint_title_text_view.text = foodJointModel.foodJointName
        viewHolder.itemView.food_joint_description_text_view.text =
            foodJointModel.foodJointDescription
        viewHolder.itemView.food_joint_image_view.setImageDrawable(context.resources.getDrawable(R.drawable.restaurant))

        viewHolder.itemView.food_joint_image_view.setOnClickListener { openMenuFragment() }
        viewHolder.itemView.food_joint_card_bottom_layout.setOnClickListener { openMenuFragment() }
    }

    private fun openMenuFragment() {
        val fragmentActivity = context as FragmentActivity
        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val foodMenuFragment = FoodMenuFragment()
        fragmentTransaction.replace(R.id.frame, foodMenuFragment)
        fragmentTransaction.commit()
    }
}