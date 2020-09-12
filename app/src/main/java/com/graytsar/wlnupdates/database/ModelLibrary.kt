package com.graytsar.wlnupdates.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemLibrary")
class ModelLibrary(
    @PrimaryKey(autoGenerate = true) var pk: Long,
    @ColumnInfo(name = "idWlnupdates") var idWlnupdates: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "cover") var cover: String,
    @ColumnInfo(name = "position") var position: Long,
    @ColumnInfo(name = "isNotificationEnabled") var isNotificationEnabled: Boolean,
    @ColumnInfo(name = "volume") var volume: Double,
    @ColumnInfo(name = "chapter") var chapter: Double
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readInt(),
        source.readString()!!,
        source.readString()!!,
        source.readLong(),
        1 == source.readInt(),
        source.readDouble(),
        source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(pk)
        writeInt(idWlnupdates)
        writeString(title)
        writeString(cover)
        writeLong(position)
        writeInt((if (isNotificationEnabled) 1 else 0))
        writeDouble(volume)
        writeDouble(chapter)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ModelLibrary> = object : Parcelable.Creator<ModelLibrary> {
            override fun createFromParcel(source: Parcel): ModelLibrary = ModelLibrary(source)
            override fun newArray(size: Int): Array<ModelLibrary?> = arrayOfNulls(size)
        }
    }
}