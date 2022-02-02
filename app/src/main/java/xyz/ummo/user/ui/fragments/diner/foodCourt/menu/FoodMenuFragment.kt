package xyz.ummo.user.ui.fragments.diner.foodCourt.menu

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_food_menu.view.*
import timber.log.Timber
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentFoodCourtTabbedBinding
import xyz.ummo.user.databinding.FragmentFoodMenuBinding
import xyz.ummo.user.models.FoodMenuModel
import xyz.ummo.user.rvItems.FoodMenuItem
import xyz.ummo.user.ui.fragments.diner.foodCourt.FoodCourtFragment
import xyz.ummo.user.utilities.mode
import xyz.ummo.user.utilities.ummoUserPreferences

class FoodMenuFragment : Fragment() {
    private lateinit var viewBinding: FragmentFoodMenuBinding
    private lateinit var foodCourtFragmentBinding: FragmentFoodCourtTabbedBinding
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tabbedToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        gAdapter = GroupAdapter()

        mixpanelAPI = MixpanelAPI
            .getInstance(context, context?.resources?.getString(R.string.mixpanelToken))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_menu, container, false)
        foodCourtFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_court_tabbed, container, false)

        rootView = viewBinding.root
        sharedPreferences = context?.getSharedPreferences(ummoUserPreferences, mode)!!

        /** Scaffolding the [recyclerView] **/
        recyclerView = rootView.food_item_recycler_view
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = rootView.service_category_recycler_view.layoutManager
//        recyclerView.layoutManager = rootView.food_item_recycler_view.layoutManager
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = gAdapter

        getFoodMenu()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Hiding the MainActivity toolbar **/
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        foodCourtFragmentBinding.dinerScreenAppBar.orderTrackerAppBarLayout.visibility =
            View.VISIBLE

        viewBinding.foodMenuToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        viewBinding.foodMenuToolBar.setNavigationOnClickListener { openFragment(FoodCourtFragment()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE

//        tabbedToolbar = context.applicationContext.re
        foodCourtFragmentBinding.dinerScreenAppBar.orderTrackerAppBarLayout.visibility = View.GONE

    }

    private fun getFoodMenu() {

        viewBinding.loadMenuProgressBar.visibility = View.GONE

        gAdapter.add(
            0, FoodMenuItem(
                FoodMenuModel(
                    "123",
                    "French Fries",
                    "The crispiest fries around Kwalu",
                    requireContext().resources.getDrawable(R.drawable.fries),
                    "E10-E50",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            1, FoodMenuItem(
                FoodMenuModel(
                    "234",
                    "Fried Chicken",
                    "Sweet and sour chicken",
                    requireContext().resources.getDrawable(R.drawable.chicken_fried_rice),
                    "E25-E35",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            2, FoodMenuItem(
                FoodMenuModel(
                    "345",
                    "Mexican Beef",
                    "Rich spicy Mexican been",
                    requireContext().resources.getDrawable(R.drawable.mexican_beef_stew),
                    "E25-E50",
                    true
                ), requireContext()
            )
        )

        gAdapter.add(
            3, FoodMenuItem(
                FoodMenuModel(
                    "345",
                    "Caesar's Salad",
                    "Caesar always munched on these greens",
                    requireContext().resources.getDrawable(R.drawable.greek_salad),
                    "E10-E15",
                    true
                ), requireContext()
            )
        )
    }

    private fun openFragment(fragment: Fragment) {

        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager
            .beginTransaction()

        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.diner_joint_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_diner_menu -> {
                Timber.e("Sharing Diner Menu")
                true
            }
            R.id.save_diner_menu -> {
                Timber.e("Saving Diner Menu")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FoodMenuFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}