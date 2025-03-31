package com.example.ap_application_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ap_application_mobile.ui.theme.AP_Application_MobileTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.getStringExtra("email") ?: "Email inconnu"
        val pseudo = intent.getStringExtra("pseudo") ?: "Pseudo inconnu"
        val reservations: ArrayList<Reservation>? = intent.getParcelableArrayListExtra("reservations")


        setContent {
            AP_Application_MobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(email, pseudo, reservations ?: arrayListOf())
                }
            }
        }
    }
}

@Composable
fun HomeScreen(email: String, pseudo: String, reservations: List<Reservation>) {
    val backgroundImage = painterResource(id = R.drawable.image)
    Image(
        painter = backgroundImage,
        contentDescription = "Image de fond",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenue, $pseudo", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        if (reservations.isNotEmpty()) {
            LazyColumn {
                items(reservations) { reservation ->
                    ReservationItem(reservation)
                }
            }
        } else {
            Text(text = "Aucune réservation trouvée.", fontSize = 18.sp)
        }
    }
}

@Composable
fun ReservationItem(reservation: Reservation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Forfait: ${reservation.forfait_nom}", fontSize = 18.sp)
            Text(text = "Code d'accès: ${reservation.access_code}", fontSize = 18.sp)
            Text(text = "Temps restant: ${reservation.temps_restant ?: "Indisponible"}", fontSize = 18.sp)
        }
    }
}
