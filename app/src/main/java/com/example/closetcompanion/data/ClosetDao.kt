package com.example.closetcompanion.data

import androidx.room.*

@Dao
interface ClosetDao {
    @Query("SELECT * FROM closets")
    suspend fun getAllClosets(): List<Closet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCloset(closet: Closet)

    @Delete
    suspend fun deleteCloset(closet: Closet)

    @Query("SELECT * FROM closets WHERE id=:id")
    suspend fun getClosetById(id: Int): Closet?

    @Query("SELECT * FROM closets WHERE name = :name")
    suspend fun getClosetByName(name: String): Closet?

    @Query("SELECT * FROM outfits WHERE id IN (SELECT outfit_ids FROM closets WHERE id=:id)")
    suspend fun getOutfitsByClosetId(id: Int): List<Outfit>
}