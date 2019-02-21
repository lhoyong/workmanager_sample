package com.github.lhoyong.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var response: String

    private val workManager = WorkManager.getInstance()

    companion object {
        private const val WORKER_REPEAT = "repeat"

        private const val WORKER_ONETIME = "onetime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onetime.setOnClickListener {
            oneTimeWorker()
        }

        repeat_keep.setOnClickListener { repeatKeepWorker() }
    }

    // start one time working
    private fun oneTimeWorker() {

        // create workRequest
        val workRequest = OneTimeWorkRequestBuilder<SampleWorker>().addTag(WORKER_ONETIME).build()

        workManager.let {

            // work request enqueue
            it.beginUniqueWork(WORKER_ONETIME, ExistingWorkPolicy.KEEP, workRequest).enqueue()

            val statusLiveData = it.getWorkInfoByIdLiveData(workRequest.id)

            statusLiveData.observe(this, Observer {

                if (it != null && it.state.isFinished) {
                    Log.e("oneTimeWorkerStatus", "${it.state}")
                }
            })


        }

    }

    private fun repeatKeepWorker() {

        // check network connect
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        // make request
        val workRequest =
            PeriodicWorkRequestBuilder<SampleWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(WORKER_REPEAT)
                .build()

        workManager.let {

            it.enqueueUniquePeriodicWork(WORKER_REPEAT, ExistingPeriodicWorkPolicy.KEEP, workRequest)

            val statusLiveData = it.getWorkInfoByIdLiveData(workRequest.id)

            statusLiveData.observe(this, Observer {

                if (it != null && it.state.isFinished) {
                    Log.e("KeepWorkerObservable", "$it")
                }

            })
        }
    }
}
