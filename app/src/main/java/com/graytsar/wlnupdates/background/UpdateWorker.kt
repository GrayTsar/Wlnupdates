package com.graytsar.wlnupdates.background

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.R
import com.graytsar.wlnupdates.channelID
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.notificationID
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestNovel

class UpdateWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {

        Log.d("DBG", "doWork1111111111111")

        val listLibrary = DatabaseService.db?.daoLibrary()!!.selectAllAsList()
        val listNotify = ArrayList<ModelLibrary>()

        listLibrary.forEach {
            if(it.isNotificationEnabled){
                listNotify.add(it)
            }
        }

        if (listNotify.isEmpty()){
            return Result.success()
        }

        listNotify.forEach { model ->
            val request = RestService.restService.getNovel(RequestNovel(model.idWlnupdates)).execute().body()
            request?.let { response ->
                if(response.error == false){
                    response.dataNovel?.let { dataNovel ->
                        dataNovel.releases?.let { listRelease ->
                            val release = listRelease.firstOrNull()
                            release?.let { release ->
                                var volume:Double = 0.0
                                var chapter:Double = 0.0

                                release.volume?.let {
                                    volume = it
                                }
                                release.chapter?.let {
                                    chapter = it
                                }

                                if(volume > model.volume){
                                    //notify

                                    if(context is MainActivity){
                                        context.pushNotify(model.title, "Volume ${model.volume}")
                                    }
                                }

                                if(chapter > model.chapter) {
                                    if(context is MainActivity){
                                        context.pushNotify(model.title, "Chapter ${model.chapter}")
                                    }
                                }

                                model.volume = volume
                                model.chapter = chapter

                                DatabaseService.db?.daoLibrary()!!.update(model)

                                sendNotification(model.title, "Volume ${model.volume}, Chapter ${model.chapter}")
                            }
                        }
                    }
                }
            }
        }

        Log.d("DBG", "endWork222222")
        return Result.success()
    }

    private fun sendNotification(title: String, text: String){
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, channelID).apply {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(R.drawable.ic_app)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            //setDeleteIntent(pendingDeleteIntent)
            setChannelId(channelID)
        }.build()

        notificationManager.notify(notificationID, notification)
    }
}