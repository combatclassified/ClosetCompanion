package com.example.closetcompanion.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "clothes")
data class Clothes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageData: ByteArray,
    val name: String,
    val type: String,
    val size: String,
    val color: String,
    val status: String
) : Serializable