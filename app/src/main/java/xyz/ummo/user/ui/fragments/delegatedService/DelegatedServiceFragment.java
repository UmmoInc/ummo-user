package xyz.ummo.user.ui.fragments.delegatedService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.ummo.user.DelegationChat;
import xyz.ummo.user.R;
import xyz.ummo.user.data.entity.DelegatedServiceEntity;
import xyz.ummo.user.ui.detailedService.DetailedProductViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DelegatedServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DelegatedServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DelegatedServiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private String agentName, productName, serviceId, serviceAgentId, delegatedProductId;

    private TextView agentNameTextView, agentStatusTextView,
            delegatedProductNameTextView, delegatedProductDescriptionTextView,
            delegatedProductCostTextView, delegatedProductDurationTextView,
            delegatedServiceDocsTextView, delegatedServiceStepsTextView;

    ArrayList<String> stepsList;
    ArrayList<String> docsList;

    private LinearLayout delegatedProductDocsLayout, delegatedProductStepsLayout;

    private ImageView openChat;

    private final int mode = Activity.MODE_PRIVATE;
    private final String ummoUserPreferences = "UMMO_USER_PREFERENCES";

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "DelegatedServiceFragmen";
    private DelegatedServiceViewModel delegatedServiceViewModel;
    private DetailedProductViewModel detailedProductViewModel;
    private DelegatedServiceEntity delegatedServiceEntity = new DelegatedServiceEntity();

    public DelegatedServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DelegatedServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DelegatedServiceFragment newInstance(String param1, String param2) {
        DelegatedServiceFragment fragment = new DelegatedServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        if (getArguments() != null) {

            SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(ummoUserPreferences, mode);
            agentName = sharedPreferences.getString("DELEGATED_AGENT","");

            serviceId = getArguments().getString("SERVICE_ID");
            serviceAgentId = getArguments().getString("SERVICE_AGENT_ID");
            delegatedProductId = getArguments().getString("DELEGATED_PRODUCT_ID");

            delegatedServiceEntity.setServiceId(serviceId);
            delegatedServiceEntity.setServiceAgentId(serviceAgentId);
            delegatedServiceEntity.setDelegatedProductId(delegatedProductId);

            delegatedServiceViewModel = ViewModelProviders.of(this)
                    .get(DelegatedServiceViewModel.class);
            delegatedServiceViewModel.insertDelegatedService(delegatedServiceEntity);

            detailedProductViewModel = ViewModelProviders.of(this)
                    .get(DetailedProductViewModel.class);
        }

        Log.e(TAG, "onCreate: arguments: SERVICE-ID->"+serviceId);
        Log.e(TAG, "onCreate: arguments: SERVICE-AGENT-ID->"+serviceAgentId);
        Log.e(TAG, "onCreate: arguments: DELEGATED-PRODUCT-ID->"+delegatedProductId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delegated, container, false);
        agentNameTextView = view.findViewById(R.id.delegated_agent_name_text_view);
        agentStatusTextView = view.findViewById(R.id.delegated_agent_status_text_view);
        openChat = view.findViewById(R.id.open_chat_button);

        goToDelegationChat(view);

        agentNameTextView.setText(agentName);

        delegatedProductNameTextView = view.findViewById(R.id.delegated_service_header_name);
        delegatedProductDescriptionTextView = view.findViewById(R.id.description_text_view);
        delegatedProductCostTextView = view.findViewById(R.id.service_cost_text_view);
        delegatedProductDurationTextView = view.findViewById(R.id.service_duration_text_view);

        delegatedProductDocsLayout = view.findViewById(R.id.service_docs_linear_layout);
        delegatedProductStepsLayout = view.findViewById(R.id.delegated_service_steps_layout);

        detailedProductViewModel.getDelegatedProduct(false).observe(this, delegatedProductEntity ->{
            Log.e(TAG, "onCreateView: DELEGATED_ID->"+delegatedProductEntity.getProductName());
        });

        delegatedServiceViewModel
                .getDelegatedServiceByProductId(delegatedProductId).observe(this, delegatedServiceEntity1 -> {
            Log.e(TAG, "onCreateView: DelegatedServiceModel"+delegatedServiceEntity1.getServiceAgentId());
        });

        detailedProductViewModel.getProductEntityLiveDataById(delegatedProductId).observe(this, delegatedProductEntity -> {

            Log.e(TAG, "onCreateView: DELEGATED PRODUCT->"+delegatedProductEntity.getProductName());
            delegatedProductNameTextView.setText(delegatedProductEntity.getProductName());
            delegatedProductDescriptionTextView.setText(delegatedProductEntity.getProductDescription());
            delegatedProductCostTextView.setText(delegatedProductEntity.getProductCost());
            delegatedProductDurationTextView.setText(delegatedProductEntity.getProductDuration());

            docsList = new ArrayList<>(delegatedProductEntity.getProductDocuments());
            stepsList = new ArrayList<>(delegatedProductEntity.getProductSteps());
            if (!docsList.isEmpty()){

                for (int i = 0; i < docsList.size(); i++){
                    delegatedServiceDocsTextView = new TextView(getContext());
                    delegatedServiceDocsTextView.setId(i);
                    delegatedServiceDocsTextView.setText(delegatedProductEntity.getProductDocuments().get(i));
                    delegatedServiceDocsTextView.setTextSize(14);
                    delegatedProductDocsLayout.addView(delegatedServiceDocsTextView);
                }
            }

            if (!stepsList.isEmpty()){

                for (int i = 0; i < stepsList.size(); i++){
                    delegatedServiceStepsTextView = new TextView(getContext());
                    delegatedServiceStepsTextView.setId(i);
                    delegatedServiceStepsTextView.setText(delegatedProductEntity.getProductSteps().get(i));
                    delegatedServiceStepsTextView.setTextSize(14);
                    delegatedProductStepsLayout.addView(delegatedServiceStepsTextView);
                }
            }
        });

        return view;
    }

    private void goToDelegationChat(View view){
        openChat = view.findViewById(R.id.open_chat_button);

        delegatedServiceViewModel
                .getDelegatedServiceByProductId(delegatedProductId).observe(this, delegatedServiceEntity1 -> {
            Log.e(TAG, "goToDelegationChat: DelegatedServiceModel"+delegatedServiceEntity1.getDelegatedProductId());
            delegatedServiceEntity1.getServiceId();
        });

        detailedProductViewModel
                .getDelegatedProduct(false).observe(this, delegatedProductEntity ->{
            Log.e(TAG, "onCreateView: DELEGATED_ID->"+delegatedProductEntity.getProductName());
            productName = delegatedProductEntity.getProductName();
        });

        SharedPreferences delegatedServiceFragPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences(ummoUserPreferences, mode);

        if (getArguments() != null) {
            serviceId = getArguments().getString("SERVICE_ID");
            agentName = delegatedServiceFragPrefs.getString("DELEGATED_AGENT","");
        }

        openChat.setOnClickListener(v -> {
            Intent chatIntent = new Intent(getActivity(), DelegationChat.class);
            chatIntent.putExtra("AGENT_NAME", agentName);
            chatIntent.putExtra("SERVICE_ID", serviceId);
            chatIntent.putExtra("SERVICE_NAME", productName);
            startActivity(chatIntent);
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.e(TAG, "onAttach: LISTENER->"+mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
