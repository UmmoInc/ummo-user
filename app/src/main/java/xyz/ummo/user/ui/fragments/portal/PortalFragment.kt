package xyz.ummo.user.ui.fragments.portal

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_portal.view.*
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentPortalBinding
import xyz.ummo.user.models.ServicePortalModel
import xyz.ummo.user.rvItems.ServicePortalItem
import xyz.ummo.user.utilities.USER_NAME
import xyz.ummo.user.utilities.mode
import xyz.ummo.user.utilities.ummoUserPreferences

class PortalFragment : Fragment() {
    private lateinit var viewBinding: FragmentPortalBinding
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        gAdapter = GroupAdapter()

        getPortals()

        mixpanelAPI = MixpanelAPI
            .getInstance(context, context?.resources?.getString(R.string.mixpanelToken))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_portal,
            container,
            false
        )

        rootView = viewBinding.root

        sharedPreferences = context?.getSharedPreferences(ummoUserPreferences, mode)!!

        userName = sharedPreferences.getString(USER_NAME, "").toString()
        val endOfFirstName = userName.indexOf(" ", 0, true)
        val firstName = userName.substring(0, endOfFirstName)

        /** Scaffolding the [recyclerView] **/
        recyclerView = rootView.service_portal_recycler_view
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = rootView.service_category_recycler_view.layoutManager
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = gAdapter

        viewBinding.portalBarTitleTextView.text = "Welcome, $firstName"

        return rootView
    }

    private fun getPortals() {
        gAdapter = GroupAdapter()
        gAdapter.add(
            0, ServicePortalItem(
                ServicePortalModel(
                    "DINE",
                    "Treat your taste buds", 3
                ),
                requireContext()
            )
        )
        gAdapter.add(
            1, ServicePortalItem(
                ServicePortalModel(
                    "GOV",
                    "Get the most from govt.", 15
                ),
                requireContext()
            )
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EntranceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PortalFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}