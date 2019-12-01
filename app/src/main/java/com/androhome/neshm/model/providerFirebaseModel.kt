package com.androhome.neshm.model

data class providerFirebaseModel(
    val endDate: String = "",
    val startDate: String = "",
    val endTime: String = "",
    val startTime: String = "",
    val endtimeStamp: Long = 0,
    val startTimeStamp: Long = 0,
    val studentName: String = "",
    val phoneNumber: String = "",
    val acceptance: String = "",
    val payment: String = "",
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val landmark: String = ""
)