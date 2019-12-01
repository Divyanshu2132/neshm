package com.androhome.neshm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androhome.neshm.fragments.ForgotPasswordFragment
import com.androhome.neshm.fragments.OtpFragment
import com.androhome.neshm.fragments.SignUpFragment
import com.androhome.neshm.interfaces.LoginApiInterface
import com.androhome.neshm.model.loginModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {
    private lateinit var forgotPassword: TextView
    private lateinit var signUp: TextView
    private lateinit var submit: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: ProgressBar

    val loginApiInterface by lazy {
        LoginApiInterface.create()
    }
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        onClicks()
    }

    private fun initViews() {
        forgotPassword = findViewById(R.id.forgotPasswordText)
        signUp = findViewById(R.id.SignUpText)
        submit = findViewById(R.id.submit_button)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    fun swapFragmenr(fragment: Fragment, args: Bundle?, previousFragment: Fragment?) {
        if (args != null) {
            fragment.arguments = args
        }
        if (previousFragment != null) {
            supportFragmentManager.beginTransaction().remove(previousFragment).commitAllowingStateLoss()
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager.beginTransaction().replace(R.id.contentFrame, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commitAllowingStateLoss()
    }

    fun onClicks() {
        forgotPassword.setOnClickListener(View.OnClickListener {
            swapFragmenr(ForgotPasswordFragment(), null, null)
        })
        signUp.setOnClickListener(View.OnClickListener {
            swapFragmenr(
                SignUpFragment(), null, null
            )
        })
        submit.setOnClickListener(View.OnClickListener {
            //            val intent: Intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            val emailEntered: String = email.text.toString()
            val passwordEntered: String = password.text.toString()
            if (!emailEntered.isEmpty()) {
                if (!passwordEntered.isEmpty()) {
                    login(emailEntered, passwordEntered)
                } else {
                    Toast.makeText(applicationContext, "Password can't be empty", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Email can't be empty", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun login(emailEntered: String, passwordEntered: String) {
        progressBar.visibility = View.VISIBLE
        forgotPassword.isEnabled = false
        submit.isEnabled = false
        signUp.isEnabled = false
        disposable = loginApiInterface.login(emailEntered, passwordEntered).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                Log.e("login success", result.userInfo?.get(0)?.email + " ")
                if (!(result.userInfo?.get(0)?.email).toString().isEmpty() && (result.userInfo?.get(0)?.email) != null) {
                    saveUserData(result.userInfo)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if ((result.userInfo?.get(0)?.result).toString().equals("Email not verified")) {
//                    Toast.makeText(applicationContext, "Verify email ", Toast.LENGTH_LONG).show()
                    var bundle: Bundle = Bundle()
                    bundle.putString("User id", emailEntered)
                    progressBar.visibility = View.GONE
                    forgotPassword.isEnabled = true
                    submit.isEnabled = true
                    signUp.isEnabled = true
                    swapFragmenr(OtpFragment(), bundle, null)
                }
            }, { error ->
                Toast.makeText(applicationContext, "Unable to login", Toast.LENGTH_LONG).show()
                Log.e("Login Error", error.message)
                progressBar.visibility = View.GONE
                forgotPassword.isEnabled = true
                submit.isEnabled = true
                signUp.isEnabled = true
            })
    }

    private fun saveUserData(result: List<loginModel.userInfo>?) {
        val sharedPref = applicationContext.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref.edit()) {
            putString("email", result?.get(0)?.email)
            putString("phone number", result?.get(0)?.phoneNumber.toString())
            putString("name", result?.get(0)?.name)
            putString("role", result?.get(0)?.role)
            putString("houseno", result?.get(0)?.houseNo)
            putString("street", result?.get(0)?.street)
            putString("area", result?.get(0)?.area)
            putString("city", result?.get(0)?.city)
            putString("pincode", result?.get(0)?.pincode)
            putString("state", result?.get(0)?.state)
            putString("landmark", result?.get(0)?.landmark)
            putString("lat", result?.get(0)?.latitude)
            putString("lang", result?.get(0)?.longitude)
            putString("tempState", result?.get(0)?.state)
            putBoolean("logged in", true)
            apply()
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
