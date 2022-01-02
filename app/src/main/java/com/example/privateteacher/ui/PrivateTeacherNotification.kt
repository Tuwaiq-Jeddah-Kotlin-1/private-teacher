package com.example.privateteacher.ui

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.privateteacher.MainActivity

class PrivateTeacherNotification {

    private val mainActivity=MainActivity()
    fun myNotification(massage:String){
        val myWorkRequest= OneTimeWorkRequestBuilder<PrivateTeacherWorker>()
            .setInputData(workDataOf(
                "title" to "privateTeacher",
                "message" to massage)
            )
            .build()
        WorkManager.getInstance(mainActivity)
            .enqueue(myWorkRequest)
    }

}