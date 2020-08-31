package com.graytsar.wlnupdates.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.graytsar.wlnupdates.MainActivity
import com.graytsar.wlnupdates.database.DatabaseService
import com.graytsar.wlnupdates.database.ModelLibrary
import com.graytsar.wlnupdates.rest.interfaces.RestService
import com.graytsar.wlnupdates.rest.request.RequestNovel

class UpdateWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
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
                if(response.error!! == false){
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
                            }
                        }
                    }
                }
            }
        }

        return Result.success()
    }
}