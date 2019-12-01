package com.androhome.neshm.model

import com.google.gson.annotations.SerializedName

object SignupModel {
    class status {
        @SerializedName("status")
        var status: List<result>? = null
    }

    class result {
        @SerializedName("Result")
        var result: String? = null
    }
}