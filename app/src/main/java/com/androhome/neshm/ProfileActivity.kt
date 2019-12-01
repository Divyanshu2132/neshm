package com.androhome.neshm

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ProfileActivity : AppCompatActivity() {
    private lateinit var emailText: TextView
    private lateinit var nameText: TextView
    private lateinit var phoneText: TextView
    private lateinit var addressText: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews()
        listeners()
        setValues()
    }

    private fun listeners() {
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }

    private fun setValues() {
        val sharedPref = applicationContext.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        ) ?: return
        val email: String? = sharedPref.getString("email", "-")
        val phone: String? = sharedPref.getString("phone number", "-")
        val name: String? = sharedPref.getString("name", "-")
        val address: String? = sharedPref.getString("houseno", "-") + "," +
                sharedPref.getString("street", "-") + "," +
                sharedPref.getString("area", "-") + "," +
                sharedPref.getString("city", "-") + "," +
                sharedPref.getString("state", "-")
        emailText.text = email
        phoneText.text = phone
        addressText.text = address
        nameText.text = name
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        emailText = findViewById(R.id.email)
        nameText = findViewById(R.id.name)
        phoneText = findViewById(R.id.phone_number)
        addressText = findViewById(R.id.address)
    }
}
