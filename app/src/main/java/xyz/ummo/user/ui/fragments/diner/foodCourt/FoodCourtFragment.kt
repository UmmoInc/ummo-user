package xyz.ummo.user.ui.fragments.diner.foodCourt

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.food_court_fragment.view.*
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FoodCourtFragmentBinding
import xyz.ummo.user.models.FoodJointModel
import xyz.ummo.user.rvItems.FoodJointItem
import xyz.ummo.user.utilities.USER_NAME
import xyz.ummo.user.utilities.mode
import xyz.ummo.user.utilities.ummoUserPreferences

class FoodCourtFragment : Fragment() {
    private lateinit var viewBinding: FoodCourtFragmentBinding
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var toolbar: MaterialToolbar

    companion object {
        fun newInstance() = FoodCourtFragment()
    }

    private lateinit var viewModel: FoodCourtViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gAdapter = GroupAdapter()

        getFoodJoints()

        mixpanelAPI = MixpanelAPI
            .getInstance(context, context?.resources?.getString(R.string.mixpanelToken))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.food_court_fragment,
            container,
            false
        )
        rootView = viewBinding.root
        sharedPreferences = context?.getSharedPreferences(ummoUserPreferences, mode)!!
        userName = sharedPreferences.getString(USER_NAME, "").toString()
        val endOfFirstName = userName.indexOf(" ", 0, true)
        val firstName = userName.substring(0, endOfFirstName)

        /** Scaffolding the [recyclerView] **/
        recyclerView = rootView.food_court_recycler_view
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = rootView.service_category_recycler_view.layoutManager
        recyclerView.layoutManager = rootView.food_court_recycler_view.layoutManager
        recyclerView.adapter = gAdapter

//        viewBinding.homeBarTitleTextView.text = "Welcome, $firstName"

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Hiding the MainActivity toolbar **/
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

//        viewBinding.foodCourt
    }

    /*override fun onDestroyView() {
        super.onDestroyView()

        toolbar.visibility = View.VISIBLE
    }*/

    private fun getFoodJoints() {
        gAdapter.add(
            0, FoodJointItem(
                FoodJointModel(
                    "1234",
                    "Maphanga's Fast-Food",
                    "Come enjoy the best French Fries in Matsapha",
                    resources.getDrawable(R.drawable.restaurant),
                    8,
                    8
                ), requireContext()
            )
        )

        gAdapter.add(
            1, FoodJointItem(
                FoodJointModel(
                    "2345",
                    "Fifty's Fast-Food",
                    "Come shawty, it's your lunch time",
                    resources.getDrawable(R.drawable.restaurant),
                    8,
                    8
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoodCourtViewModel::class.java)
        // TODO: Use the ViewModel
    }

}