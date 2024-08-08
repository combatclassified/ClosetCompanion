package com.example.closetcompanion.data

import androidx.room.*

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfits")
    suspend fun getAllOutfits(): List<Outfit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: Outfit)

    @Delete
    suspend fun deleteOutfit(outfit: Outfit)

    @Query("SELECT * FROM outfits WHERE id=:id")
    suspend fun getOutfitById(id: Int): Outfit?

    @Query("SELECT * FROM outfits WHERE name = :name")
    suspend fun getOutfitByName(name: String): Outfit?

    @Query("SELECT * FROM clothes WHERE id IN (SELECT clothes_id FROM outfit_clothes_join WHERE outfit_id=:id)")
    suspend fun getClothesByOutfitId(id: Int): List<Clothes>
}