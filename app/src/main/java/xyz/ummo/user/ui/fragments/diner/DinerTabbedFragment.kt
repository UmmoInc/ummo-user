package xyz.ummo.user.ui.fragments.diner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import xyz.ummo.user.R
import xyz.ummo.user.adapters.PagesViewPagerAdapter
import xyz.ummo.user.databinding.FragmentFoodCourtTabbedBinding
import xyz.ummo.user.ui.fragments.diner.drinkingWell.DrinkingWellFragment
import xyz.ummo.user.ui.fragments.diner.foodCourt.FoodCourtFragment
import xyz.ummo.user.ui.fragments.diner.snackBar.SnackBarFragment

class DinerTabbedFragment : Fragment() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var viewBinding: FragmentFoodCourtTabbedBinding
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_food_court_tabbed,
            container,
            false
        )

        rootView = viewBinding.root

        /** Setting up Diner Tabs **/
        setupDinerTabs()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Hiding the MainActivity toolbar **/
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewBinding.dinerScreenAppBar.orderTrackerAppBarLayout.visibility = View.GONE
    }

    private fun setupDinerTabs() {
        val dineAdapter = PagesViewPagerAdapter(childFragmentManager)

        dineAdapter.addFragment(FoodCourtFragment(), "Food-Court")
        dineAdapter.addFragment(SnackBarFragment(), "Snack-Bar")
        dineAdapter.addFragment(DrinkingWellFragment(), "Drinking-Well")

        viewBinding.dineTabsViewPager.adapter = dineAdapter
        viewBinding.dineTabLayout.setupWithViewPager(viewBinding.dineTabsViewPager)
        viewBinding.dineTabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_fastfood_24)
        viewBinding.dineTabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_cookie_24)
        viewBinding.dineTabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_wine_bar_24)
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
            DinerTabbedFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}