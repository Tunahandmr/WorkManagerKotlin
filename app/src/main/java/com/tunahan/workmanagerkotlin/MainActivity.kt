package com.tunahan.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import com.tunahan.workmanagerkotlin.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = Data.Builder().putInt("intKey",1).build()
        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false).build()

        /*
        val myWorkRequest:WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
           // .setInitialDelay(5,TimeUnit.MINUTES)
            //.addTag("myTag")
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

         */

        val myWorkRequest:WorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.RUNNING){
                    println("running")
                }else if (it.state == WorkInfo.State.FAILED){
                    println("failed")
                }else if (it.state == WorkInfo.State.SUCCEEDED){
                    println("succeeded")
                }
            })

        //cancel
        //WorkManager.getInstance(this).cancelAllWork()

        //chaining - only available on OneTimeWorkRequest

        val firstRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val secondRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val thirdRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).beginWith(firstRequest)
            .then(secondRequest)
            .then(thirdRequest)
            .enqueue()
    }
}