package xyz.ummo.user.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

import xyz.ummo.user.delegate.Logout
import xyz.ummo.user.delegate.PublicServiceData
import xyz.ummo.user.ui.fragments.HomeFragment
import xyz.ummo.user.ui.fragments.LegalTermsFragment
import xyz.ummo.user.ui.fragments.ProfileFragment
import xyz.ummo.user.ui.fragments.PaymentMethodsFragment
import xyz.ummo.user.ui.fragments.ServiceHistoryFragment

import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import xyz.ummo.user.EditMyProfile
import xyz.ummo.user.R

class MainScreen : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private val fragment: Fragment? = null

    private var drawer: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var navigationHeader: View? = null
    private var navHeaderUserName: TextView? = null
    private var navHeaderUserEmail: TextView? = null
    private var toolbar: Toolbar? = null
    private var messageIconButton: ImageView? = null
    private var circularProgressBarButton: ProgressBar? = null
    var logoutLayout: LinearLayout? = null
        private set

    private var anyServiceInProgress = false
    private var serviceProgress = 0
    private var mAuth: FirebaseAuth? = null

    // flag to load home fragment when user presses back key
    private val shouldLoadHomeFragOnBackPress = true
    private var mHandler: Handler? = null
    private val mode = Activity.MODE_PRIVATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = "Ummo"
        //Log.e(TAG,"Getting USER_ID->"+new PrefManager(this).getUserId());
        object : xyz.ummo.user.delegate.PublicService(this) {
            override fun done(data: List<PublicServiceData>, code: Number) {
                loadHomeFragment(data)
                //Do something with list of services
            }
        }

        mAuth = FirebaseAuth.getInstance()

        val mainActPrefs = getSharedPreferences(ummoUserPreferences, mode)

        val userNamePref = mainActPrefs.getString("USER_NAME", "")
        val userEmailPref = mainActPrefs.getString("USER_EMAIL", "")

        Log.e(TAG, "Username->" + userNamePref!!)

        logoutClick()

        //initialise  the toolbar icons message icon and circular progress bar icon
        messageIconButton = findViewById(R.id.message_icon_button)
        circularProgressBarButton = findViewById(R.id.circular_progressbar_btn)

        circularProgressBarButton!!.progress = serviceProgress

        mHandler = Handler()

        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById(R.id.nav_view)
        navigationView!!.setNavigationItemSelectedListener(this)

        navigationHeader = navigationView!!.getHeaderView(0)
        navHeaderUserName = navigationHeader!!.findViewById(R.id.navHeaderUsername)
        navHeaderUserEmail = navigationHeader!!.findViewById(R.id.navHeaderEmail)

        navHeaderUserName!!.text = userNamePref
        navHeaderUserEmail!!.text = userEmailPref

        // initializing navigation menu
        setUpNavigationView()

        if (savedInstanceState == null) {
            navItemIndex = 0

            CURRENT_TAG = TAG_HOME

        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun displaySelectedScreen(itemId: Int) {
        var selectedFragment: Fragment? = null

        var data: List<PublicServiceData>

        when (itemId) {
            R.id.nav_home -> selectedFragment = HomeFragment()
            R.id.nav_profile -> selectedFragment = ProfileFragment()
            R.id.nav_payment_methods -> selectedFragment = PaymentMethodsFragment()
            R.id.nav_service_history -> selectedFragment = ServiceHistoryFragment()
            R.id.nav_legal_terms -> selectedFragment = LegalTermsFragment()
        }

        if (selectedFragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame, selectedFragment)
            fragmentTransaction.commit()
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_payment_methods) {

        } else if (id == R.id.nav_service_history) {

        } else if (id == R.id.nav_legal_terms) {

        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        //you can leave it empty
    }

    private fun loadHomeFragment(data: List<PublicServiceData>) {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) != null) {
            drawer!!.closeDrawers()
            return
        }
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = getHomeFragment(data)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out)
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
            fragmentTransaction.commitAllowingStateLoss()
        }

        // If mPendingRunnable is not null, then add to the message queue

        mHandler!!.post(mPendingRunnable)


        //Closing drawer on item click
        drawer!!.closeDrawers()

        // refresh toolbar menu
        invalidateOptionsMenu()
    }

    private fun getHomeFragment(data: List<PublicServiceData>): Fragment {
        when (navItemIndex) {
            0 -> {
                // home
                title = "Ummo"
                messageIconButton!!.visibility = View.VISIBLE
                circularProgressBarButton!!.visibility = View.VISIBLE

                return HomeFragment(data)
            }
            1 -> {
                // My Profile
                val myProfileFragment = ProfileFragment()

                messageIconButton!!.visibility = View.GONE
                circularProgressBarButton!!.visibility = View.GONE
                title = "Profile"

                return myProfileFragment
            }

            2 -> {
                // payment methods fragment
                val paymentMethodsFragment = PaymentMethodsFragment()
                messageIconButton!!.visibility = View.GONE
                circularProgressBarButton!!.visibility = View.GONE
                title = "Payment Method"

                return paymentMethodsFragment
            }

            3 -> {
                // service history fragment
                return ServiceHistoryFragment()
            }

            4 -> {
                // legal terms fragment
                return LegalTermsFragment()
            }

            else -> return HomeFragment(data)
        }
    }

    private fun setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView!!.setNavigationItemSelectedListener { menuItem ->

            displaySelectedScreen(menuItem.itemId)
            //Checking if the item is in checked state or not, if not make it in checked state
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true

            // loadHomeFragment(data);

            true
        }

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

        }

        //Setting the actionbarToggle to drawer layout
        drawer?.setDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }

    fun setAnyServiceInProgress(anyServiceInProgress: Boolean) {
        this.anyServiceInProgress = anyServiceInProgress
    }

    fun setServiceProgress(serviceProgress: Int) {
        this.serviceProgress = serviceProgress
    }

    fun goToEditProfile(view: View) {

        val textViewToEdit: TextView

        var textToEdit = " "
        var toolBarTitle = " "

        when (view.id) {

            R.id.full_name -> {
                textViewToEdit = view.findViewById(view.id)
                textToEdit = textViewToEdit.text.toString()
                toolBarTitle = "Enter your full name"
            }

            R.id.id_number -> {
                textViewToEdit = view.findViewById(view.id)
                textToEdit = textViewToEdit.text.toString()
                toolBarTitle = "Enter your ID Number"
            }

            R.id.contact -> {
                textViewToEdit = view.findViewById(view.id)
                textToEdit = textViewToEdit.text.toString()
                toolBarTitle = "Enter your phone number"
            }

            R.id.email -> {
                textViewToEdit = view.findViewById(view.id)
                textToEdit = textViewToEdit.text.toString()
                toolBarTitle = "Enter your email"
            }
        }

        val myProfileFragment = ProfileFragment()
        val intent = Intent(this, EditMyProfile::class.java)
        val tag = myProfileFragment.tag
        intent.putExtra(EditMyProfile.CONST_TAG, tag)
        intent.putExtra("name", textToEdit)
        intent.putExtra("toolBarTitle", toolBarTitle)
        startActivity(intent)
    }

    fun finishEditProfile(view: View) {

        val fragment = ProfileFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment, "TAG_PROFILE")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logoutClick() {
        logoutLayout = findViewById(R.id.logoutLinear)
        logoutLayout!!.setOnClickListener {
            mAuth!!.signOut()
            val progress = ProgressDialog(this@MainScreen)
            progress.setMessage("Logging out...")
            progress.show()
            object : Logout(this@MainScreen) {
                override fun done() {
                    startActivity(Intent(applicationContext, SlideIntro::class.java))
                }
            }
        }
    }

    fun logout(view: View) {
        mAuth!!.signOut()
        val progress = ProgressDialog(this@MainScreen)
        progress.setMessage("Logging out...")
        progress.show()
        object : Logout(this) {
            override fun done() {
                startActivity(Intent(this@MainScreen, SlideIntro::class.java))
            }
        }
        // prefManager.unSetFirstTimeLaunch();
    }

    companion object {
        // tags used to attach the fragments
        private val TAG_HOME = "home"
        private val TAG_PROFILE = "profile"
        private val TAG_PAYMENTS = "paymentMethods"
        private val TAG_SERVICE_HISTORY = "serviceHistory"
        private val TAG_LEGAL_TERMS = "legalTerms"
        var CURRENT_TAG = TAG_HOME

        // index to identify current nav menu item
        var navItemIndex = 0
        private const val ummoUserPreferences = "UMMO_USER_PREFERENCES"
        private const val TAG = "MainScreen"
    }
}