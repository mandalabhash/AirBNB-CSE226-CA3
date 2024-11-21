package com.aimmore.airbnv_ca3

data class PropsModel (
    val id: Long,
    val name: String,
    val location: String,
    val price: Double,
    val description: String,
    val contact: String,
    var isFavorite: Boolean = false,
    val image: ByteArray? = null
)