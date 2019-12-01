package com.androhome.neshm.model

import android.content.Context
import android.content.SharedPreferences

class UserModel(val context: Context) {
    var loggedin: Boolean = false
    var email: String = ""
    var phoneNumber: String = ""
    var name: String = ""
    var houseno: String = ""
    var street: String = ""
    var area: String = ""
    var city: String = ""
    var pincode: String = ""
    var state: String = ""
    var landmark: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var address: String = ""
    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("neshm", Context.MODE_PRIVATE)
        loggedin = sharedPreferences.getBoolean("logged in", false)
        email = sharedPreferences.getString("email", "") as String
        phoneNumber = sharedPreferences.getString("phone number", "") as String
        houseno = sharedPreferences.getString("houseno", "") as String
        street = sharedPreferences.getString("street", "") as String
        area = sharedPreferences.getString("area", "") as String
        city = sharedPreferences.getString("city", "") as String
        pincode = sharedPreferences.getString("pincode", "") as String
        state = sharedPreferences.getString("state", "") as String
        landmark = sharedPreferences.getString("landmark", "") as String
        latitude = (sharedPreferences.getString("lat", "0.0") as String).toDouble()
        longitude = (sharedPreferences.getString("lang", "0.0") as String).toDouble()
        address = houseno + ", " + street + ", " + area + ", " + city + ", " + state
    }
}