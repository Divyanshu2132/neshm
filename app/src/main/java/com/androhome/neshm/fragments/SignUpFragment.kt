package com.androhome.neshm.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androhome.neshm.LoginActivity
import com.androhome.neshm.R
import io.reactivex.disposables.Disposable


class SignUpFragment : Fragment() {


    private lateinit var email: EditText
    private lateinit var name: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var submitButton: Button
    var disposable: Disposable? = null
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        initView(view)
        listeners()
        return view
    }

    private fun listeners() {
        submitButton.setOnClickListener(View.OnClickListener {
            var emailEntered: String = email.text.toString()
            var nameEntered: String = name.text.toString()
            var phoneNumberEntered: String = phoneNumber.text.toString()
            var passwordEntered: String = password.text.toString()

            var confirmPasswordEntered: String = confirmPassword.text.toString()
            if (!emailEntered.isEmpty() && !nameEntered.isEmpty() && !phoneNumberEntered.isEmpty() && !passwordEntered.isEmpty() && !confirmPasswordEntered.isEmpty()) {
                if (passwordEntered.equals(confirmPasswordEntered)) {
                    var bundle = Bundle()
                    bundle.putString("email", emailEntered)
                    bundle.putString("name", nameEntered)
                    bundle.putString("phone", phoneNumberEntered)
                    bundle.putString("password", passwordEntered)
                    (activity as LoginActivity).swapFragmenr(AddressFragment(), bundle, null)
                    submitButton.isEnabled = false
                } else {
                    Toast.makeText(requireContext(), "Password didn't match", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Fill up all the fields", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initView(view: View) {
        email = view.findViewById(R.id.email)
        name = view.findViewById(R.id.name)
        phoneNumber = view.findViewById(R.id.phone_number)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirm_password)
        submitButton = view.findViewById(R.id.submit_button)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }


}
