package xyz.ummo.user.ui.fragments.pagesFrags

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home_affairs.view.*
import timber.log.Timber
import xyz.ummo.user.R
import xyz.ummo.user.data.entity.ServiceEntity
import xyz.ummo.user.data.entity.ServiceProviderEntity
import xyz.ummo.user.databinding.FragmentHomeAffairsBinding
import xyz.ummo.user.models.Service
import xyz.ummo.user.rvItems.ServiceItem
import xyz.ummo.user.ui.viewmodels.ServiceProviderViewModel
import xyz.ummo.user.ui.viewmodels.ServiceViewModel

class HomeAffairsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var homeAffairsBinding: FragmentHomeAffairsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var gAdapter: GroupAdapter<GroupieViewHolder>

    /** ServiceProvider ViewModel && Entity Declarations **/
    private var serviceProviderViewModel: ServiceProviderViewModel? = null

    /** Service ViewModel && Entity Declarations **/
    private var serviceViewModel: ServiceViewModel? = null

    /** HomeAffairs Service instance && Service ID **/
    private lateinit var homeAffairsServiceId: String
    private lateinit var homeAffairsService: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gAdapter = GroupAdapter()

        /** Initializing ViewModels: ServiceProvider && Services **/
        serviceProviderViewModel = ViewModelProvider(this)
                .get(ServiceProviderViewModel::class.java)

        serviceViewModel = ViewModelProvider(this)
                .get(ServiceViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        homeAffairsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home_affairs,
                container, false)

        val view = homeAffairsBinding.root
        val layoutManager = view.services_recycler_view.layoutManager

        recyclerView = view.services_recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = gAdapter

        getHomeAffairsServiceProviderId()
        getHomeAffairsServices(homeAffairsServiceId)

        return view
    }

    private fun getHomeAffairsServiceProviderId() {
        val serviceProviders: List<ServiceProviderEntity>? = serviceProviderViewModel
                ?.getServiceProviderList()

        for (i in serviceProviders?.indices!!) {
            Timber.e("SERVICE-PROVIDERS [2]=> ${serviceProviders[i].serviceProviderId}")
            when {
                serviceProviders[i].serviceProviderName
                        .equals("ministry of home affairs", true) -> {

                    homeAffairsServiceId = serviceProviders[i].serviceProviderId.toString()
                    Timber.e("Home Affairs ID [2] -> $homeAffairsServiceId")
                }
            }
        }
    }

    private fun getHomeAffairsServices(homeAffairsId: String) {
        var serviceId: String
        var serviceName: String
        var serviceDescription: String
        var serviceEligibility: String
        var serviceCentre: String
        var presenceRequired: Boolean
        var serviceCost: String
        var serviceRequirements: String
        var serviceDuration: String
        var approvalCount: Int
        var disApprovalCount: Int
        var commentCount: Int
        var shareCount: Int
        var viewCount: Int

        val servicesList = serviceViewModel?.getServicesList()
        for (i in servicesList?.indices!!) {
            if (servicesList[i].serviceProvider == homeAffairsId) {
                Timber.e("HOME AFFAIRS SERVICE [3] -> ${servicesList[i].serviceName}")
                serviceId = servicesList[i].serviceId.toString() //0
                serviceName = servicesList[i].serviceName.toString() //1
                serviceDescription = servicesList[i].serviceDescription.toString() //2
                serviceEligibility = servicesList[i].serviceEligibility.toString() //3
                serviceCentre = servicesList[i].serviceCentres.toString() //4
                presenceRequired = servicesList[i].presenceRequired!! //5
                serviceCost = servicesList[i].serviceCost.toString() //6
                serviceRequirements = servicesList[i].serviceDocuments.toString() //7
                serviceDuration = servicesList[i].serviceDuration.toString() //8
                approvalCount = servicesList[i].approvalCount!! //9
                disApprovalCount = servicesList[i].disapprovalCount!! //10
                commentCount = servicesList[i].comments?.size!! //11
                shareCount = servicesList[i].serviceShares!! //12
                viewCount = servicesList[i].serviceViews!! //13

                homeAffairsService = Service(serviceId, serviceName, serviceDescription,
                        serviceEligibility, serviceCentre, presenceRequired, serviceCost,
                        serviceRequirements, serviceDuration, approvalCount, disApprovalCount,
                        commentCount, shareCount, viewCount)
                Timber.e("HOME-AFFAIRS-SERVICE-BLOB [1] -> $homeAffairsService")

                gAdapter.add(ServiceItem(homeAffairsService, context))

                homeAffairsBinding.loadProgressBar.visibility = View.GONE

                recyclerView.adapter = gAdapter
            }
        }
    }

    private fun showSnackbarBlue(message: String, length: Int) {
        /** Length is 0 for Snackbar.LENGTH_LONG
         *  Length is -1 for Snackbar.LENGTH_SHORT
         *  Length is -2 for Snackbar.LENGTH_INDEFINITE**/
        val bottomNav = requireActivity().findViewById<View>(R.id.bottom_nav)
        val snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), message, length)
        snackbar.setTextColor(resources.getColor(R.color.ummo_4))
        snackbar.anchorView = bottomNav
        snackbar.show()
    }

    companion object {

        fun newInstance() = HomeAffairsFragment()

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeAffairsFragment().apply {

                }
    }
}