package com.androhome.neshm.model

import com.google.gson.annotations.SerializedName

object loginModel {
    class allUsers {
        @SerializedName("all_users")
        var userInfo: List<userInfo>? = null
    }

    class userInfo {
        @SerializedName("Result")
        var result: String? = null
        @SerializedName("Role")
        var role: String? = null
        @SerializedName("Name")
        var name: String? = null
        @SerializedName("Email")
        var email: String? = null
        @SerializedName("Phone")
        var phoneNumber: Long? = null
        @SerializedName("Houseno")
        var houseNo: String? = null
        @SerializedName("Street")
        var street: String? = null
        @SerializedName("Area")
        var area: String? = null
        @SerializedName("City")
        var city: String? = null
        @SerializedName("Pincode")
        var pincode: String? = null
        @SerializedName("State")
        var state: String? = null
        @SerializedName("Landmark")
        var landmark: String? = null
        @SerializedName("lat")
        var latitude: String? = null
        @SerializedName("lang")
        var longitude: String? = null
    }
}