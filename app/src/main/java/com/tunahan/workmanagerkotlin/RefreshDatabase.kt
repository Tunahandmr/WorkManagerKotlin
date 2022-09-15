package com.tunahan.workmanagerkotlin

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {

        val getData = inputData
        val mNumber = getData.getInt("intKey",0)
        refreshDatabase(mNumber)
        return Result.success()
    }

    private fun refreshDatabase(mNumber: Int){

        val sharedPreferences = context.getSharedPreferences("com.tunahan.workmanagerkotlin",Context.MODE_PRIVATE)
        var number = sharedPreferences.getInt("myNumber",0)
        number += 1
        println(number)
        sharedPreferences.edit().putInt("myNumber",number).apply()
    }
}