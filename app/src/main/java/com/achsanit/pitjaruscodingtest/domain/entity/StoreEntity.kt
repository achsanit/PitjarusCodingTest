package com.achsanit.pitjaruscodingtest.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "store_table")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "store_id")
    val storeId: Int,

    @ColumnInfo("store_name")
    val storeName: String,

    @ColumnInfo("latitude")
    val latitude: String,

    @ColumnInfo("longitude")
    val longitude: String,

    @ColumnInfo("isVisited")
    val visited: Boolean = false,
): Parcelable
