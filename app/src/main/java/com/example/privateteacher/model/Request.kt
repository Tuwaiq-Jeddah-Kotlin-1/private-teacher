package com.example.privateteacher.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val current = LocalDateTime.now()

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

val formatted = current.format(formatter)
@Parcelize
data class Request (
var dateReq:String=formatted,
var idrequest:String="",
    var subject:String="",
    var timeVal:Int=0,
    var teacherUid:String="",
    var studentUid:String="",
var state:String="pending",
        ):Parcelable
