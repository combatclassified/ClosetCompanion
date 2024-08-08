package com.example.closetcompanion.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "closet_outfit_join",
    primaryKeys = ["closet_id", "outfit_id"],
    foreignKeys = [
        ForeignKey(
            entity = Closet::class,
            parentColumns = ["id"],
            childColumns = ["closet_id"]
        ),
        ForeignKey(
            entity = Outfit::class,
            parentColumns = ["id"],
            childColumns = ["outfit_id"]
        )
    ]
)
data class ClosetOutfitJoin(
    val closet_id: Int,
    val outfit_id: Int
)