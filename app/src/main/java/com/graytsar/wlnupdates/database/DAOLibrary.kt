package com.graytsar.wlnupdates.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAOLibrary {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(novel: ModelLibrary):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(novel: ModelLibrary)

    @Delete
    fun delete(novel: ModelLibrary)


    @Query("SELECT * FROM ItemLibrary")
    fun selectAll(): LiveData<List<ModelLibrary>>

    @Query("SELECT * FROM ItemLibrary")
    fun selectAllAsList(): List<ModelLibrary>

    @Query("SELECT * FROM ItemLibrary WHERE idWlnupdates == :id")
    fun selectWhereIdWlnupdates(id:Int): List<ModelLibrary>
}