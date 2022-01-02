package com.example.privateteacher.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Student(
   // var id: Int?,
    var name: String="",
    var email: String="",
    var level:String=""
    ): Parcelable

