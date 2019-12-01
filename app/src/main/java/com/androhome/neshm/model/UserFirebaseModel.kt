package com.androhome.neshm.model

data class UserFirebaseModel(
    val endDate: String = "", val startDate: String = "", val endTime: String = "", val startTime: String = "",
    val endtimeStamp: Long = 0, val startTimeStamp: Long = 0, val tutorName: String = "", val phoneNumber: String = "",
    val acceptance: String = "", val payment: String = ""
)