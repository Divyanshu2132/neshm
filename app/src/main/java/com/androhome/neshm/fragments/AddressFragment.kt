package com.androhome.neshm.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androhome.neshm.R
import com.androhome.neshm.interfaces.LoginApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddressFragment : Fragment() {

    var disposable: Disposable? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var submitButton: Button
    private lateinit var houseno: EditText
    private lateinit var steet: EditText
    private lateinit var area: EditText
    private lateinit var city: EditText
    private lateinit var state: EditText
    private lateinit var pincode: EditText
    private lateinit var landmark: EditText
    private var email: String = ""
    private var name: String = ""
    private var phone: String = ""
    private var password: String = ""

    val loginApiInterface by lazy {
        LoginApiInterface.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_address, container, false)
        initViews(view)
        listeners()
        return view
    }

    private fun listeners() {

    }

    private fun initViews(view: View) {
        var bundle: Bundle? = arguments
        email = bundle?.getString("email") as String
        name = bundle.getString("name") as String
        phone = bundle.getString("phone") as String
        password = bundle.getString("password") as String
        progressBar = view.findViewById(R.id.progress_bar)
        submitButton = view.findViewById(R.id.submit_button)
        houseno = view.findViewById(R.id.houseno)
        steet = view.findViewById(R.id.street)
        area = view.findViewById(R.id.area)
        city = view.findViewById(R.id.city)
        state = view.findViewById(R.id.state)
        pincode = view.findViewById(R.id.pincode)
        landmark = view.findViewById(R.id.landmark)
    }


    private fun signup(
        emailEntered: String,
        phoneNumberEntered: String,
        nameEntered: String,
        passwordEntered: String,
        houseno: String,
        street: String,
        area: String,
        city: String,
        state: String,
        pincode: String,
        landmark: String
    ) {
        progressBar.visibility = View.VISIBLE
        disposable =
            loginApiInterface.signIn("Skill Seeker", emailEntered, phoneNumberEntered, nameEntered, passwordEntered)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                    Log.e("signup success", "user added successfully")
                    if (result.status?.get(0)?.result.equals("Record Added Successfully")) {
//                        (activity as LoginActivity).onBackPressed() todo this line on address call
                    } else {
                        Toast.makeText(requireContext(), "Unable to signup", Toast.LENGTH_LONG).show()
                        submitButton.isEnabled = true
                        progressBar.visibility = View.GONE
                    }
                }, { error ->
                    Toast.makeText(requireContext(), "Unable to signup", Toast.LENGTH_LONG).show()
                    Log.e("Login Error", error.message)
                    submitButton.isEnabled = true
                    progressBar.visibility = View.GONE
                })
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }
}
