package xyz.ummo.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import xyz.ummo.user.delegate.PublicServiceData;
import xyz.ummo.user.fragments.HomeFragment;
import xyz.ummo.user.fragments.LegalTermsFragment;
import xyz.ummo.user.fragments.MyProfileFragment;
import xyz.ummo.user.fragments.PaymentMethodsFragment;
import xyz.ummo.user.fragments.ServiceHistoryFragment;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainScreen extends AppCompatActivity
        implements MyProfileFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private Fragment fragment;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView messageIconButton;
    private ProgressBar circularProgressBarButton;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_PAYMENTS = "paymentMethods";
    private static final String TAG_SERVICE_HISTORY = "serviceHistory";
    private static final String TAG_LEGAL_TERMS = "legalTerms";
    public static String CURRENT_TAG = TAG_HOME;

    private boolean anyServiceInProgress = false;
    private int serviceProgress = 0;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    private int mode = Activity.MODE_PRIVATE;
    private static final String ummoUserPreferences = "UMMO_USER_PREFERENCES";
    private static final String TAG = "MainScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ummo");
        Log.e(TAG,"Getting USER_ID->"+new PrefManager(this).getUserId());
        new xyz.ummo.user.delegate.PublicService(){
            @Override
            public void done(@NotNull List<PublicServiceData> data, @NotNull Number code) {
                loadHomeFragment(data);
                //Do something with list of services
            }
        };

        SharedPreferences mainActPrefs = getSharedPreferences(ummoUserPreferences, mode);

        String userNamePref = mainActPrefs.getString("USER_NAME", "");
        Log.e(TAG, "Username->"+userNamePref);


        //initialise  the toolbar icons message icon and circular progress bar icon
        messageIconButton = findViewById(R.id.message_icon_button);
        circularProgressBarButton = findViewById(R.id.circular_progressbar_btn);

        circularProgressBarButton.setProgress(serviceProgress);

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;

            CURRENT_TAG = TAG_HOME;

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_payment_methods) {

        } else if (id == R.id.nav_service_history) {

        }
        else if (id == R.id.nav_legal_terms) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void loadHomeFragment(List data) {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment(data);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment(List data) {
        switch (navItemIndex) {
            case 0:
                // home
                setTitle("Ummo");
                messageIconButton.setVisibility(View.VISIBLE);
                circularProgressBarButton.setVisibility(View.VISIBLE);
                HomeFragment homeFragment = new HomeFragment(data);

                return homeFragment;
            case 1:
                // My Profile
                MyProfileFragment myProfileFragment = new MyProfileFragment();

                messageIconButton.setVisibility(View.GONE);
                circularProgressBarButton.setVisibility(View.GONE);
                setTitle("Profile");

                return myProfileFragment;

            case 2:
                // payment methods fragment
                PaymentMethodsFragment paymentMethodsFragment = new PaymentMethodsFragment();
                messageIconButton.setVisibility(View.GONE);
                circularProgressBarButton.setVisibility(View.GONE);
                setTitle("Payment Method");

                return paymentMethodsFragment;

            case 3:
                // service history fragment
                ServiceHistoryFragment serviceHistoryFragment = new ServiceHistoryFragment();
                return serviceHistoryFragment;

            case 4:
                // legal terms fragment
                LegalTermsFragment legalTermsFragment= new LegalTermsFragment();
                return legalTermsFragment;
 
            default:
                return new HomeFragment(data);
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_payment_methods:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PAYMENTS;
                        break;
                    case R.id.nav_service_history:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SERVICE_HISTORY;
                        break;
                    case R.id.nav_legal_terms:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_LEGAL_TERMS;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

               // loadHomeFragment(data);

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    public void setAnyServiceInProgress(boolean anyServiceInProgress) {
        this.anyServiceInProgress = anyServiceInProgress;
    }

    public void setServiceProgress(int serviceProgress) {
        this.serviceProgress = serviceProgress;
    }

    public void goToEditProfile(View view){

        TextView textViewToEdit;

        String textToEdit = " ", toolBarTitle = " ";

        switch(view.getId()){

            case R.id.full_name:
                textViewToEdit = view.findViewById(view.getId());
                textToEdit = textViewToEdit.getText().toString();
                toolBarTitle = "Enter your full name";

                break;

            case R.id.id_number:
                textViewToEdit = view.findViewById(view.getId());
                textToEdit = textViewToEdit.getText().toString();
                toolBarTitle = "Enter your ID Number";

                break;

            case R.id.contact:
                textViewToEdit = view.findViewById(view.getId());
                textToEdit = textViewToEdit.getText().toString();
                toolBarTitle = "Enter your phone number";

                break;

            case R.id.email:
                textViewToEdit = view.findViewById(view.getId());
                textToEdit = textViewToEdit.getText().toString();
                toolBarTitle = "Enter your email";

                break;
        }

        MyProfileFragment myProfileFragment  = new MyProfileFragment();
        Intent intent= new Intent(this, EditMyProfile.class);
        String tag = myProfileFragment.getTag();
        intent.putExtra(EditMyProfile.CONST_TAG, tag);
        intent.putExtra("name", textToEdit);
        intent.putExtra("toolBarTitle", toolBarTitle);
        startActivity(intent);
    }

    public void finishEditProfile(View view){

        Fragment fragment = new MyProfileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, "TAG_PROFILE");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
