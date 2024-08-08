package com.example.closetcompanion.data

import androidx.room.*

@Dao
interface ClosetOutfitJoinDao {
    @Query("SELECT outfit_id FROM closet_outfit_join WHERE closet_id = :closetId")
    suspend fun getOutfitIdsForCloset(closetId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClosetOutfitJoin(closetOutfitJoin: ClosetOutfitJoin)

    @Delete
    suspend fun deleteClosetOutfitJoin(closetOutfitJoin: ClosetOutfitJoin)
}