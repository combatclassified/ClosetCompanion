package com.example.closetcompanion.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "outfit_clothes_join",
    primaryKeys = ["outfit_id", "clothes_id"],
    foreignKeys = [
        ForeignKey(
            entity = Outfit::class,
            parentColumns = ["id"],
            childColumns = ["outfit_id"]
        ),
        ForeignKey(
            entity = Clothes::class,
            parentColumns = ["id"],
            childColumns = ["clothes_id"]
        )
    ]
)
data class OutfitClothesJoin(
    val outfit_id: Int,
    val clothes_id: Int
)