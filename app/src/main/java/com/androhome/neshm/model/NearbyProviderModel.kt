package com.androhome.neshm.model

import com.google.gson.annotations.SerializedName

object NearbyProviderModel {
    class allUsers {
        @SerializedName("all_users")
        var userInfo: List<providerInfo>? = null
    }

    class providerInfo {
        @SerializedName("Result")
        var result: String? = null
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
        @SerializedName("Userid")
        var userId: String? = null
        @SerializedName("Profile Pic")
        var profilePic: String? = null
        @SerializedName("Genre")
        var genre: String? = null
        @SerializedName("SubCategory")
        var subCategory: String? = null
        @SerializedName("Fees")
        var fees: String? = null
    }
}