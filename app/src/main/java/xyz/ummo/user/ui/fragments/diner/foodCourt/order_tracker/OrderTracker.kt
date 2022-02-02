package xyz.ummo.user.ui.fragments.diner.foodCourt.order_tracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.kofigyan.stateprogressbar.StateProgressBar
import com.mixpanel.android.mpmetrics.MixpanelAPI
import timber.log.Timber
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentOrderTrackerBinding

class OrderTracker : Fragment() {

    private lateinit var viewBinding: FragmentOrderTrackerBinding
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String
    private lateinit var toolbar: MaterialToolbar
    private lateinit var appbar: AppBarLayout
    private lateinit var stateProgressBar: StateProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        mixpanelAPI = MixpanelAPI
            .getInstance(context, context?.resources?.getString(R.string.mixpanelToken))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_tracker, container, false)
        rootView = viewBinding.root
        stateProgressBar = viewBinding.orderStateProgressBar

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Hiding the MainActivity toolbar **/
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE

        appbar = requireActivity().findViewById(R.id.main_app_bar)
        appbar.visibility = View.GONE

//        viewBinding.orderTrackerToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
//        viewBinding.orderTrackerToolbar.setNavigationOnClickListener { openFragment() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE

        viewBinding.orderScreenAppBar.orderTrackerAppBarLayout.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.order_tracker_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_order_diner -> {
                Timber.e("Sharing Order")
                true
            }
            R.id.diner_order_support -> {
                Timber.e("Diner Order Support")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            OrderTracker().apply {
                arguments = Bundle().apply {

                }
            }
    }
}