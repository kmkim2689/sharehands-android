package com.sharehands.sharehands_frontend.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo_item")
data class MemoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "memo") val memo: String
)
