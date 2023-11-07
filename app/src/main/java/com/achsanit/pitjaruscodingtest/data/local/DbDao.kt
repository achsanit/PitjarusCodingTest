package com.achsanit.pitjaruscodingtest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.achsanit.pitjaruscodingtest.domain.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListStore(listStore: List<StoreEntity>)

    @Query("SELECT * FROM store_table")
    fun getListStore(): Flow<List<StoreEntity>>

    @Query("DELETE FROM store_table")
    fun deleteAllCandy()

    @Transaction
    suspend fun deleteAndInsertStore(listStore: List<StoreEntity>) {
        deleteAllCandy()
        insertListStore(listStore)
    }

    @Query("UPDATE store_table SET isVisited = :isVisited WHERE id = :id")
    suspend fun updateStatusVisit(isVisited: Boolean, id: Int)
}