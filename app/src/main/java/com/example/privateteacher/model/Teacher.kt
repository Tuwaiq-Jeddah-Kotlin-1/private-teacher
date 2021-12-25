package com.example.privateteacher.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Teacher(
    var teacherId: String = "",
    var name: String = "",
    var email: String = "",
    var major: String = "",
    var subject: String = "",
    var level: String = "",
    var startTime: Long = 0,
    var endTime: Long = 0,
    var phoneNumber: String = "",
) : Parcelable