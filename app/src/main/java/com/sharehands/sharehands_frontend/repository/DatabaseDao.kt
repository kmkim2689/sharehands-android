package com.sharehands.sharehands_frontend.repository

import androidx.room.*
import org.apache.commons.lang3.mutable.Mutable

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment")
    fun getAllEquipments(): MutableList<Equipment>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEquipment(equipment: Equipment)

    @Delete
    fun deleteEquipment(equipment: Equipment)
}

@Dao
interface MemoItemDao {
    @Query("SELECT * FROM memo_item")
    fun getAllMemos(): MutableList<MemoItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMemo(memoItem: MemoItem)

    @Delete
    fun deleteMemo(memoItem: MemoItem)
}