package com.example.closetcompanion.fragments.RecyclerView

import java.io.Serializable

data class closetItem(
    val title: String,
    val type: String,
    val color: String,
    val size: String,
    val status: String,
    val user: String,
    val image: String,
): Serializable