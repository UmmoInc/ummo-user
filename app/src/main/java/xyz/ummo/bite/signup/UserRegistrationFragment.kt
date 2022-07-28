package xyz.ummo.bite.signup

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation



import com.google.android.material.textfield.TextInputEditText

import timber.log.Timber
import xyz.ummo.bite.R
import xyz.ummo.bite.databinding.FragmentUserRegistrationBinding
import xyz.ummo.bite.localdatabase.models.Profile
import xyz.ummo.bite.localdatabase.viewmodels.profileViewModel
import xyz.ummo.bite.sharedpreferences.Prefs
import xyz.ummo.bite.utils.constants.Constants.Companion.CHARACTER_COUNT_PHONE

class UserRegistrationFragment : Fragment() {
    private lateinit var _binding: FragmentUserRegistrationBinding
    private val binding get() = _binding!!
    private lateinit var mBundle: Bundle
    private lateinit var rootView: View
    private var passwordEndIconChangeHelper = true
    private lateinit var  mprofileViewModel: profileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
        rootView = binding.root
        mBundle = Bundle()
        termsAndConditions()
        mprofileViewModel = ViewModelProvider(this ).get(profileViewModel::class.java)
         navigationControls()
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            checkIfAllTextboxesFilled()


         })
        return rootView
    }

    private fun passFormDatatoSharedPreferences(){

        val preferences: Prefs = Prefs(requireContext())

        binding.apply{


            preferences.editor().putString("Name",registrationName.text.toString().trim()).apply()
            preferences.editor().putString("Surname",registrationSurname.text.toString().trim()).apply()
            preferences.editor().putString("Phone",registrationMomoPhone.text.toString().trim()).apply()
            preferences.editor().putString("Email",registrationEmailAddress.text.toString().trim()).apply()
            preferences.editor().putString("Password",registrationPassword.text.toString().trim()).apply()

        }

    }

    private fun addProfile() {

        val phonecontact: String = binding.registrationMomoPhone.text.toString()

        val name: String=binding.registrationName.text.toString()

        val surname: String= binding.registrationSurname.text.toString()

        val email: String=binding.registrationEmailAddress.text.toString()
        val IsPhoneverified=false
        val isPhoneSignedIn=false
        val password: String=binding.registrationPassword.text.toString()
        val isExpectingOrder=false

      try {
          val profileObject= Profile(0,phonecontact, name, surname, email, IsPhoneverified, isPhoneSignedIn, password, isExpectingOrder)
          mprofileViewModel.addProfile(profileObject)
          Toast.makeText(requireContext(), "added:${profileObject.toString()}", Toast.LENGTH_SHORT).show()
      }
        catch (ex:Exception){
            Toast.makeText(requireContext(), "could not add profile to db->$ex", Toast.LENGTH_SHORT).show()

        }

    }

    private fun setupPasswordTextinput() {
        binding.apply {

            passwordTextinput.setEndIconOnClickListener {
                if (passwordEndIconChangeHelper) {
                    passwordTextinput.setEndIconDrawable(R.drawable.password_visibilty)
                    passwordEndIconChangeHelper = false
                } else {
                    passwordTextinput.setEndIconDrawable(R.drawable.password_visibilty_off)
                    passwordEndIconChangeHelper = true
                }
            }


        }
    }

    private fun navigationControls() {

        // to Log In Fragment
        binding.logInText.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_userRegistrationFragment_to_signInFragment)


        })

        binding.appBar.setNavigationOnClickListener(View.OnClickListener {

            Navigation.findNavController(it)
                .navigate(R.id.action_userRegistrationFragment_to_signInFragment)
        })
//To OTP Fragment


    }

    private fun inputValidator(
        name: TextInputEditText, surname: TextInputEditText,
        mobile: TextInputEditText, email: TextInputEditText,
        password: TextInputEditText
    ): Boolean {
        val condition1: Boolean =
            !name.text.isNullOrEmpty() && !surname.text.isNullOrEmpty() && !mobile.text.isNullOrEmpty()
        val condition2: Boolean = !email.text.isNullOrEmpty() && !password.text.isNullOrEmpty()


        return !(condition1 && condition2)

    }


    private fun isPhoneValid(text: TextInputEditText?): Boolean {

        //check if in error state and give feedback to user

        return text != null && text.length() == CHARACTER_COUNT_PHONE

    }

    private fun termsAndConditions() {
        binding.legalTerms.isClickable = true
        binding.legalTerms.movementMethod = LinkMovementMethod.getInstance()
        val legalTerms =
            "<div>By signing up, you agree to Ummo's <a href='https://sites.google.com/view/ummo-terms-and-conditions/home'>Terms of Use</a> & <a href='https://sites.google.com/view/ummo-privacy-policy/home'> Privacy Policy </a></div>"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.legalTerms.text = Html.fromHtml(legalTerms, Html.FROM_HTML_MODE_LEGACY)

            Timber.e("USING HTML FLAG")
        } else {
            binding.legalTerms.text = Html.fromHtml(legalTerms)
            Timber.e("NOT USING HTML FLAG")
        }

    }

    private fun checkIfAllTextboxesFilled() {
        val EditTextsNOTfilled = inputValidator(
            binding.registrationName,
            binding.registrationSurname,
            binding.registrationEmailAddress,
            binding.registrationMomoPhone,
            binding.registrationPassword
        )
        if (!EditTextsNOTfilled) {
            //Check if Phone number is valid
            if (isPhoneValid(binding.registrationMomoPhone)) {

                checkIfEmailisValid()


            } else {// pin invalid
                binding.registrationMomoPhone.isFocusable = true
                binding.registrationMomoPhone.requestFocus()
                binding.registrationMomoPhone.error=getString(R.string.phonenumber_supplied_error)
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields ", Toast.LENGTH_SHORT)
                .show()


        }
    }


    private fun checkIfEmailisValid() {
        val emailField: EditText = binding.registrationEmailAddress
        val userEmail: String = binding.registrationEmailAddress.text.toString()

        when {
            Patterns.EMAIL_ADDRESS.matcher(userEmail).matches().not() -> {
                emailField.error = "Please use a valid email..."
                emailField.requestFocus()
            }
            emailField.length() == 0 -> {
                emailField.error = "Please provide an email..."
                emailField.requestFocus()
            }
            else -> {
              //  addProfile()
                passFormDatatoSharedPreferences()
                moveToOTPfragment()
            }
        }
    }

    private fun moveToOTPfragment() {
        // GET SUPPLIED PHONE NUMBER
        val phone = binding.registrationMomoPhone.text.toString()
        mBundle.putString("phone_key", "+268$phone")
        // navigate to OTP fragment : prepare  verify user
        Navigation.findNavController(rootView)
            .navigate(R.id.action_userRegistrationFragment_to_splashScreenOTPFragment, mBundle)
    }
}