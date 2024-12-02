package kiet.imam.chatkaro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kiet.imam.chatkaro.Screens.ChatRoomList
import kiet.imam.chatkaro.Screens.ChatScreen
import kiet.imam.chatkaro.Screens.SignInScreen
import kiet.imam.chatkaro.Screens.SignUpScreen
import kiet.imam.chatkaro.ui.theme.ChatKaroTheme
import kiet.imam.chatkaro.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel : AuthViewModel = viewModel()
            ChatKaroTheme {

      NavigationGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController,
                    authViewModel: AuthViewModel){
    NavHost(navController = navController,
        startDestination = Screen.SignupScreen.route) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen(authViewModel = AuthViewModel(),
                onNavigateToLogin = {
                    navController.navigate(Screen.SignIn.route)
                }
            )

        }

        composable(Screen.SignIn.route) {
            SignInScreen(authViewModel,
                onSignInSuccess = {
                                  navController.navigate(Screen.ChatRoomsScreen.route)
                },

                onNavigateToSignUp = {
                    navController.navigate(Screen.SignupScreen.route)
                }
            )

            }
        composable(Screen.ChatRoomsScreen.route){
            ChatRoomList{
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }
        composable("${Screen.ChatScreen.route}/{roomId}") {
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId)
        }
    }
}
