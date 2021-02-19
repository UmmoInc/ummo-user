package xyz.ummo.user.ui.signup

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mixpanel.android.mpmetrics.MixpanelAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import xyz.ummo.user.R
import xyz.ummo.user.databinding.RegisterBinding
import xyz.ummo.user.api.SafetyNetReCAPTCHA
import xyz.ummo.user.ui.legal.PrivacyPolicy
import xyz.ummo.user.utilities.broadcastreceivers.ConnectivityReceiver
import xyz.ummo.user.utilities.eventBusEvents.NetworkStateEvent
import xyz.ummo.user.utilities.eventBusEvents.RecaptchaStateEvent
import xyz.ummo.user.utilities.eventBusEvents.SocketStateEvent
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: RegisterBinding
    private lateinit var fullFormattedPhoneNumber: String
    private var userName: String = ""

    //Firebase Phone Auth Variables
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mVerificationId: String? = "default"
    private var mVerificationInProgress = false
    private var snackbar: Snackbar? = null
    private val connectivityReceiver = ConnectivityReceiver()
    private val recaptchaStateEvent = RecaptchaStateEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Init'ing Firebase Auth **/
        mAuth = FirebaseAuth.getInstance()

        /** Hiding the toolbar **/
        try {
            this.supportActionBar?.hide()
        } catch (npe: NullPointerException) {
            Timber.e("NPE-> $npe")
        }

        /** Binding the view to registerBinding **/
        registerBinding = RegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        /**[NetworkStateEvent-1] Register for EventBus events **/
        EventBus.getDefault().register(this)

        termsAndConditions()
        initCallback()
        register()
    }

    private fun termsAndConditions() {
        val mixpanel = MixpanelAPI.getInstance(applicationContext,
                resources.getString(R.string.mixpanelToken))

        val legalIntent = Intent(this, PrivacyPolicy::class.java)
        legalIntent.action = Intent.ACTION_VIEW

        registerBinding.legalTermsTextView.isClickable = true
        registerBinding.legalTermsTextView.movementMethod = LinkMovementMethod.getInstance()
        val legalTerms = "<div>By signing up, you agree to Ummo's <a href='https://sites.google.com/view/ummo-terms-and-conditions/home'>Terms of Use</a> & <a href='https://sites.google.com/view/ummo-privacy-policy/home'> Privacy Policy </a></div>"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerBinding.legalTermsTextView.text = Html.fromHtml(legalTerms, Html.FROM_HTML_MODE_LEGACY)

            Timber.e("USING HTML FLAG")
        } else {
            registerBinding.legalTermsTextView.text = Html.fromHtml(legalTerms)
            Timber.e("NOT USING HTML FLAG")
        }
    }

    override fun onStart() {
        super.onStart()
        /** [NetworkStateEvent-2] Registering the Connectivity Broadcast Receiver - to monitor the network state **/
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)

    }

    /** [NetworkStateEvent-3] Subscribing to the NetworkState Event (via EventBus) **/
    @Subscribe
    fun onNetworkStateEvent(networkStateEvent: NetworkStateEvent) {
        Timber.e("ON-EVENT -> ${networkStateEvent.noConnectivity}")

        /** Toggling between network states && displaying an appropriate Snackbar **/
        if (networkStateEvent.noConnectivity!!) {
            showSnackbarRed("Please check connection...", -2)
        } else {
            showSnackbarBlue("Connecting...", -1)
        }
    }

    @Subscribe
    fun onSocketStateEvent(socketStateEvent: SocketStateEvent) {
        Timber.e("SOCKET-EVENT -> ${socketStateEvent.socketConnected}")

        if (!socketStateEvent.socketConnected!!) {
            showSnackbarRed("Can't reach Ummo network", -2)
        } else {
            showSnackbarBlue("Ummo network found...", -1)
        }
    }

    override fun onStop() {
        super.onStop()
        /** [NetworkStateEvent-4] Unregistering the Connectivity Broadcast Receiver - app is in the background,
         * so we don't need to stay online (for NOW) **/
        unregisterReceiver(connectivityReceiver)

        Timber.e("onSTOP")
    }

    //TODO: Reload user details from savedInstanceState bundle - instead of re-typing
    override fun onResume() {
        super.onResume()
    }

    /** Begin Phone Number Verification with PhoneAuthProvider from Firebase **/
    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks)
        mVerificationInProgress = true
        Timber.e("Verification started!")
    }

    /** Signing up with PhoneAuth Credential - with Firebase's Auth instance (mAuth);
     * used by `initCallback()
     * **/
    private fun signUpWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showSnackbarBlue("Successfully signing in...", -1)
            } else {
                showSnackbarRed("Try again!", 0)

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    showSnackbarRed("Invalid code...", 0)
                }

                Timber.i("Sign in failed -> ${task.exception}")
                showSnackbarRed("Sign in failed!", -2)
            }
        }
    }

    private fun initCallback() {
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                showSnackbar("onVerificationCompleted:$credential", -1)
                Timber.e("onVerificationCompleted: $credential")
                // [START_EXCLUDE silent]
                mVerificationInProgress = false

                // [END_EXCLUDE]

                signUpWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // [START_EXCLUDE silent]
                showSnackbar("onVerificationFailed", 0)
                Timber.e("onVerificationFailed: $e")
                mVerificationInProgress = false
                // [END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    showSnackbar("Invalid phone number.", 0)
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showSnackbar("Quota exceeded.", 0)
                }

                // Show a message and update the UI
                showSnackbar("Verification failed", -2)

            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                showSnackbar("onCodeSent:$verificationId", -1)
                Timber.e("onCodeSent: $verificationId")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                showSnackbar("Code timeout", 0)
                Timber.e("onCodeAutoRetrievalTimeOut: $verificationId")
            }
        }
        // [END phone_auth_callbacks]
    }

    /**
     * Register function takes the contact entered by the user & parses it into an official contact by
     * checking it against the international standards for mobile numbers
     * **/
    private fun register() {
        val mixpanel = MixpanelAPI.getInstance(applicationContext,
                resources.getString(R.string.mixpanelToken))

        registerBinding.registerButton.setOnClickListener {

            registerBinding.registrationCcp.registerCarrierNumberEditText(registerBinding.userContactTextInputEditText)
            fullFormattedPhoneNumber = registerBinding.registrationCcp.fullNumberWithPlus.toString().trim()
            userName = registerBinding.userNameTextInputEditText.text.toString().trim()

            if (userName.isBlank()) {
                showSnackbar("Please enter your name.", 0)
                registerBinding.userNameTextInputEditText.error = "Enter your name here..."
            } else if (userName.length < 3) {
                registerBinding.userNameTextInputEditText.error = "Please enter your real name..."
            }

            if (registerBinding.registrationCcp.isValidFullNumber) {
                //TODO: begin registration process
                startPhoneNumberVerification(fullFormattedPhoneNumber)

                val intent = Intent(this, ContactVerificationActivity::class.java)

                Timber.e("User Name -> $userName")
                Timber.e("User Contact -> $fullFormattedPhoneNumber")

                intent.putExtra("USER_CONTACT", fullFormattedPhoneNumber)
                intent.putExtra("USER_NAME", userName)
                startActivity(intent)

                reCAPTCHA()

                mixpanel?.track("registrationStarted_nextButton")

                finish()
            } else {
                mixpanel?.track("registrationStarted_incorrectContact")

                showSnackbar("Please enter a correct number.", 0)
                registerBinding.userContactTextInputEditText.error = "Edit your contact."
            }
        }
    }

    private fun reCAPTCHA() {
        Timber.e("reCAPTCHA SUCCESSFUL - 0!")

        SafetyNet.getClient(this).verifyWithRecaptcha("6Ldc8ikaAAAAAIYNDzByhh1V7NWcAOZz-ozv-Tno")
                .addOnSuccessListener { response ->
                    Timber.e("reCAPTCHA SUCCESSFUL - 1!")
                    val userResponseToken = response.tokenResult
                    if (response.tokenResult?.isNotEmpty() == true) {
                        Timber.e("reCAPTCHA Token -> $userResponseToken")

                        GlobalScope.launch {
                            Timber.e("reCAPTCHA Token -> $userResponseToken")

                            Timber.e("GLOBAL SCOPE THREAD NAME -> ${Thread.currentThread().name}")
                            verifyCaptchaFromServer(userResponseToken)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Timber.e("reCAPTCHA FAILED - 2!")
                    if (e is ApiException) {
                        Timber.e("reCAPTCHA ERROR -> ${CommonStatusCodes.getStatusCodeString(e.statusCode)}")
                    } else
                        Timber.e("reCAPTCHA ERROR (unknown) -> ${e.message}")
                }
    }

    private fun verifyCaptchaFromServer(responseToken: String) {
        object : SafetyNetReCAPTCHA(this, responseToken) {
            override fun done(data: ByteArray, code: Number) {
                if (code == 200) {
                    Timber.e("reCAPTCHA Verified from Server -> ${String(data)}")
                    recaptchaStateEvent.recaptchaPassed = true
                    EventBus.getDefault().post(recaptchaStateEvent)
                } else {
                    recaptchaStateEvent.recaptchaPassed = false
                    EventBus.getDefault().post(recaptchaStateEvent)
                    Timber.e("reCAPTCHA could NOT be verified -> ${String(data)}")
                }
            }
        }
    }

    //TODO: Deprecate
    private fun showSnackbar(message: String, duration: Int) {
        Snackbar.make(findViewById(android.R.id.content), message, duration).show() //LENGTH_SHORT
    }

    private fun showSnackbarRed(message: String, length: Int) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, length)
        snackbar.setTextColor(resources.getColor(R.color.quantum_googred600))
        snackbar.show()
    }

    private fun showSnackbarBlue(message: String, length: Int) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, length)
        snackbar.setTextColor(resources.getColor(R.color.ummo_4))
        snackbar.show()
    }
}