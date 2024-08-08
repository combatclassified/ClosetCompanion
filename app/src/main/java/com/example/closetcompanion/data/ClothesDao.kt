package com.example.closetcompanion.data

import androidx.room.*

@Dao
interface ClothesDao {
    @Query("SELECT * FROM clothes")
    suspend fun getAllClothes(): List<Clothes>

    @Query("SELECT * FROM clothes WHERE id = :id")
    suspend fun getClothesById(id: Int): Clothes?

    @Query("SELECT * FROM clothes WHERE name = :name")
    suspend fun getClothesByName(name: String): Clothes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClothes(clothes: Clothes)

    @Delete
    suspend fun deleteClothes(clothes: Clothes)
}