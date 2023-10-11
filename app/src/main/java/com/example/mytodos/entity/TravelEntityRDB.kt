package com.example.mytodos.entity

data class TravelEntityRDB(
    var id: Int? = null,
    var posttitle: String? = null,
    var location: String? = null,
    var username: String? = null,
    var password: String? = null,
    val imageUri: String? = null
)
