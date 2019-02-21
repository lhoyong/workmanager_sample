package com.github.lhoyong.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class SampleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {


        Log.e("SampleWorker", "isDoWork ${Calendar.getInstance().time}")

        if (isStopped) {
            Log.e("SampleWorker", "Stopped")
        }

        return Result.success()
    }

}