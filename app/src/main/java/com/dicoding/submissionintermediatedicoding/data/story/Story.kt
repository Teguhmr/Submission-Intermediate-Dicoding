package com.dicoding.submissionintermediatedicoding.data.story

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class Story(
    val createdAt: String,
    val description: String,
    @PrimaryKey val id: String,
    val name: String,
    val photoUrl: String,
    val lat: Double? = null,
    val lon: Double? = null,
)
