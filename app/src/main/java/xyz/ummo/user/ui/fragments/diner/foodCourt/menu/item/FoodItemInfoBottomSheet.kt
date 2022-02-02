package xyz.ummo.user.ui.fragments.diner.foodCourt.menu.item

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.mixpanel.android.mpmetrics.MixpanelAPI
import timber.log.Timber
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentFoodItemInfoBinding
import xyz.ummo.user.ui.fragments.diner.cart.CartFragment

class FoodItemInfoBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewBinding: FragmentFoodItemInfoBinding
    private lateinit var rootView: View
    private lateinit var serviceRequestBottomSheetPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mixpanel: MixpanelAPI
    private lateinit var addToCartButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        mixpanel = MixpanelAPI.getInstance(
            requireContext(),
            resources.getString(R.string.mixpanelToken)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_item_info, container, false)
        rootView = viewBinding.root

        /** Assigning UI elements to values **/
        addToCartButton = viewBinding.addOrderToCardButton
        addToCartButton.setOnClickListener {
            this.dismiss()
            Timber.e("CART BUTTON TAPPED!")
            openFragment(CartFragment())
        }

        return rootView
    }


    private fun openFragment(fragment: Fragment) {

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager
            .beginTransaction()

        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    companion object {

        const val TAG = "FoodItemInfoBottomSheet"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodItemInfoBottomSheet().apply {
                arguments = Bundle().apply {

                }
            }
    }
}