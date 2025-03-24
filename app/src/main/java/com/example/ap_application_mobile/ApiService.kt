import com.example.ap_application_mobile.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Récupérer tous les utilisateurs
   // @GET("users.php")
    //fun getAllUsers(): Call<List<User>>

    // Récupérer un utilisateur par ID
    //@GET("users.php")
   // fun getUserById(@Query("id") userId: Int): Call<User>

    @GET("users.php")
    fun identifyUser(@Query("email") email: String, @Query("password") password: String): Call<User>
}