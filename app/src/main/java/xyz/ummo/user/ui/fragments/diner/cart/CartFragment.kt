package xyz.ummo.user.ui.fragments.diner.cart

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mixpanel.android.mpmetrics.MixpanelAPI
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentCartBinding
import xyz.ummo.user.ui.fragments.bottomSheets.add_payment.AddPaymentBottomSheet
import xyz.ummo.user.ui.fragments.diner.foodCourt.order_tracker.OrderTracker
import xyz.ummo.user.ui.main.MainScreen.Companion.supportFM
import xyz.ummo.user.utilities.mode
import xyz.ummo.user.utilities.ummoUserPreferences

class CartFragment : Fragment() {

    private lateinit var viewBinding: FragmentCartBinding
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI
    private lateinit var toolbar: MaterialToolbar
    private lateinit var appbar: AppBarLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        toolbar = requireActivity().findViewById(R.id.toolbar)
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
        appbar = requireActivity().findViewById(R.id.main_app_bar)

        mixpanelAPI = MixpanelAPI
            .getInstance(context, context?.resources?.getString(R.string.mixpanelToken))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        rootView = viewBinding.root

        viewBinding.addPaymentOptionBtn.setOnClickListener {
            launchAddPaymentOption()
        }

        viewBinding.placeOrderBtn.setOnClickListener {
            openFragment(OrderTracker())
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Hiding the MainActivity toolbar **/
        toolbar.visibility = View.GONE
        /** Hiding the Bottom NavBar **/
        bottomNavigationView.visibility = View.GONE
        /** Hiding the AppBar Layout **/
        appbar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** Revealing the MainActivity toolbar **/
        toolbar.visibility = View.VISIBLE
        /** Revealing the Bottom NavBar **/
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun launchAddPaymentOption() {
        sharedPreferences = requireContext().getSharedPreferences(ummoUserPreferences, mode)
        editor = sharedPreferences.edit()
//        editor.putBoolean(DELEGATION_INTRO_IS_CONFIRMED, true).apply()

        val addPaymentBottomSheetFragment = AddPaymentBottomSheet()
        addPaymentBottomSheetFragment.show(
            supportFM,
            AddPaymentBottomSheet.TAG
        )
    }

    private fun openFragment(fragment: Fragment) {

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager
            .beginTransaction()

        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}