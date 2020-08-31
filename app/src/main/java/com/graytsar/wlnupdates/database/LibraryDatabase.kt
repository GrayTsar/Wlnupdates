package com.graytsar.wlnupdates.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ModelLibrary::class], version = 1, exportSchema = false)
abstract class LibraryDatabase:RoomDatabase() {
    abstract fun daoLibrary(): DAOLibrary
}