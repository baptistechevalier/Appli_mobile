package com.example.ap_application_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ap_application_mobile.ui.theme.AP_Application_MobileTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AP_Application_MobileTheme {
                LoginScreen { email, password ->
                    authenticateUser(email, password)
                }
            }
        }
    }

    private fun authenticateUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            RetrofitClient.instance.identifyUser(email, password).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null && user.id != -1) {
                            Toast.makeText(this@MainActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                                putExtra("userId", user.id)
                                putExtra("email", user.email)
                                putExtra("pseudo", user.pseudo)
                                putParcelableArrayListExtra("reservations", ArrayList(user.reservation ?: emptyList()))
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Connexion refusée", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("API", "Erreur : ${t.message}")
                    Toast.makeText(this@MainActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Animation du fond en fonction de la longueur du mot de passe
    val backgroundColor by animateColorAsState(
        targetValue = if (password.length > 5) ComposeColor(0xFFFFAE00) else ComposeColor(0xFFFAFAFA),
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 2000),
        label = "backgroundColor"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Image de fond
        val backgroundImage: Painter = painterResource(id = R.drawable.arras_game)
        Image(
            painter = backgroundImage,
            contentDescription = "Image de fond",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay pour améliorer la lisibilité
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ComposeColor.Black.copy(alpha = 0.5f)) // Assombrit légèrement l'image
        )

        // Contenu principal
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ComposeColor.White.copy(alpha = 0.7f) // 🔥 Transparence ajoutée ici
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Connectez-vous",
                        fontSize = 24.sp,
                        color = ComposeColor.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Votre identifiant") },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Votre mot de passe") },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onLogin(email, password) },
                        colors = ButtonDefaults.buttonColors(containerColor = ComposeColor.Blue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Se connecter", color = ComposeColor.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AP_Application_MobileTheme {
        LoginScreen { _, _ -> }
    }
}
