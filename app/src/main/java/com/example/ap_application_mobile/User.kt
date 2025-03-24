package com.example.ap_application_mobile

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val password: String,
    @SerializedName("reservations") val reservation: List<Reservation>?,
    val pseudo: String
)
