package com.example.ap_application_mobile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Reservation(
    val id: Int,
    val user_id: Int,
    val forfait_id: Int,
    val access_code: String,
    val created_at: String,
    val temps_restant: String?,
    val forfait_nom: String,
    val duration: String
) : Parcelable
