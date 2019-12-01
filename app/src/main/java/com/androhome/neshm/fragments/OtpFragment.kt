package com.androhome.neshm.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androhome.neshm.LoginActivity
import com.androhome.neshm.R
import com.androhome.neshm.interfaces.LoginApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OtpFragment : Fragment() {
    private lateinit var otpEditText: EditText
    private lateinit var submitButton: Button
    val loginApiInterface by lazy {
        LoginApiInterface.create()
    }
    var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_otp, container, false)
        initViews(view)
        listeners()
        return view
    }

    private fun listeners() {
        submitButton.setOnClickListener(View.OnClickListener {
            if (!otpEditText.text.toString().isEmpty()) {
                var otpEntered: Int = otpEditText.text.toString().toInt()
                var userId: String?
                var bundle: Bundle? = arguments
                if (bundle != null) {
                    userId = bundle.getString("User id")
                    if (!userId.isNullOrEmpty()) {
                        disposable = loginApiInterface.verifyOtp(userId, otpEntered).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                                Log.e("Otp Value", result.status?.get(0)?.result)
                                when {
                                    result.status?.get(0)?.result.equals("Email verified") -> {
                                        Toast.makeText(requireContext(), "Email Verified", Toast.LENGTH_LONG).show()
                                        (activity as LoginActivity).onBackPressed()
                                    }
                                    result.status?.get(0)?.result.equals("Wrong otp") -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Wrong otp",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    else -> Toast.makeText(requireContext(), "Wrong otp", Toast.LENGTH_LONG).show()
                                }
                            }, { error ->
                                Toast.makeText(requireContext(), "Unable verify otp", Toast.LENGTH_LONG).show()
                                Log.e("OTP Error", error.message)
                            })
                    }
                }
            }
        })
    }

    private fun initViews(view: View) {
        otpEditText = view.findViewById(R.id.otpEditText)
        submitButton = view.findViewById(R.id.submit_button)
    }

    override fun onPause() {
        disposable?.dispose()
        super.onPause()
    }


}
