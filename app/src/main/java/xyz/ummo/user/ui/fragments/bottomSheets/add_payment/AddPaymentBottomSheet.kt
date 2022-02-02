package xyz.ummo.user.ui.fragments.bottomSheets.add_payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mixpanel.android.mpmetrics.MixpanelAPI
import xyz.ummo.user.R
import xyz.ummo.user.databinding.FragmentAddPaymentBinding

class AddPaymentBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewBinging: FragmentAddPaymentBinding
    private lateinit var rootView: View
    private lateinit var mixpanelAPI: MixpanelAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinging =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_payment, container, false)
        rootView = viewBinging.root

        return rootView
    }

    companion object {

        const val TAG = "AddPayment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddPaymentBottomSheet().apply {
                arguments = Bundle().apply {

                }
            }
    }
}