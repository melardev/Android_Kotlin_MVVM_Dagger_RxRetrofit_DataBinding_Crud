package com.melardev.android.crud.entities


data class Todo(
    val id: Long,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createdAt: String,
    val updatedAt: String
)