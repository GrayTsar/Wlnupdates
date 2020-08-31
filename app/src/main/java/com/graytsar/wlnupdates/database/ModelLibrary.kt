package com.graytsar.wlnupdates.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemLibrary")
class ModelLibrary(
    @PrimaryKey(autoGenerate = true) var pk:Long,
    @ColumnInfo(name = "idWlnupdates") var idWlnupdates:Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "cover") var cover:String,
    @ColumnInfo(name = "position") var position:Long,
    @ColumnInfo(name = "isNotificationEnabled") var isNotificationEnabled:Boolean,
    @ColumnInfo(name = "volume") var volume:Double,
    @ColumnInfo(name = "chapter") var chapter:Double) {
}