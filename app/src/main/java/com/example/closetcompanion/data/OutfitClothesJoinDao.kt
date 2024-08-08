package com.example.closetcompanion.data

import androidx.room.*

@Dao
interface OutfitClothesJoinDao {
    @Query("SELECT clothes_id FROM outfit_clothes_join WHERE outfit_id = :outfitId")
    suspend fun getClothesIdsForOutfit(outfitId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfitClothesJoin(outfitClothesJoin: OutfitClothesJoin)

    @Delete
    suspend fun deleteOutfitClothesJoin(outfitClothesJoin: OutfitClothesJoin)
}
