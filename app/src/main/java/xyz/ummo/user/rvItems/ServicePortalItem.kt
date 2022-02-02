package xyz.ummo.user.rvItems

import android.content.Context
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.service_portal.view.*
import org.json.JSONObject
import xyz.ummo.user.R
import xyz.ummo.user.models.ServicePortalModel
import xyz.ummo.user.ui.fragments.diner.DinerTabbedFragment
import xyz.ummo.user.ui.fragments.publicServiceCategories.ServiceCategories

class ServicePortalItem(
    private val servicePortalModel: ServicePortalModel,
    val context: Context
) : Item<GroupieViewHolder>() {

    private val bundle = Bundle()

    private lateinit var mixpanelAPI: MixpanelAPI

    override fun getLayout(): Int {
        return R.layout.service_portal
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.portal_title_text_view.text = servicePortalModel.portalName
        viewHolder.itemView.portal_info_text_view.text = servicePortalModel.portalDescription
        viewHolder.itemView.portal_count_text_view.text =
            servicePortalModel.portalLocationCount.toString()

        addThumbnailsToPortals(viewHolder)
        openPortal(context, viewHolder)
    }

    private fun addThumbnailsToPortals(viewHolder: GroupieViewHolder) {
        when {
            servicePortalModel.portalName.contains("DINE", true) -> {
                viewHolder.itemView.portal_image_view
                    .setImageDrawable(
                        AppCompatResources
                            .getDrawable(context, R.drawable.ic_twotone_fastfood_24)
                    )
            }
            servicePortalModel.portalName.contains("GOV", true) -> {
                viewHolder.itemView.portal_image_view
                    .setImageDrawable(
                        AppCompatResources
                            .getDrawable(context, R.drawable.ic_twotone_gov_24)
                    )
            }
        }
    }

    private fun openPortal(context: Context, viewHolder: GroupieViewHolder) {
        val portal = JSONObject()
        mixpanelAPI =
            MixpanelAPI.getInstance(context, context.resources.getString(R.string.mixpanelToken))
        viewHolder.itemView.service_portal_card_view.setOnClickListener {
            openFragment(servicePortalModel.portalName)
            portal.put("PORTAL_NAME", servicePortalModel.portalName)
            mixpanelAPI.track("servicePortal_opened", portal)
        }
    }

    private fun openFragment(portalDoor: String) {
        val fragmentActivity = context as FragmentActivity
        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when {
            portalDoor.contains("DINE") -> {
                val diner = DinerTabbedFragment()
                fragmentTransaction.replace(R.id.frame, diner)
                fragmentTransaction.commit()
            }
            portalDoor.contains("GOV") -> {
                val serviceCategories = ServiceCategories()
                fragmentTransaction.replace(R.id.frame, serviceCategories)
                fragmentTransaction.commit()
            }
            portalDoor.contains("VOTE") -> {

            }
        }
        /*val pagesFragment = PagesFragment()
        pagesFragment.arguments?.putString(CATEGORY, servicePortalModel.portalName)

        bundle.putString(SERVICE_CATEGORY, servicePortalModel.portalName)
        pagesFragment.arguments = bundle

        fragmentTransaction.replace(R.id.frame, pagesFragment)
        fragmentTransaction.commit()*/
    }
}