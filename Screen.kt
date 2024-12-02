package kiet.imam.chatkaro

 sealed class Screen(val route:String) {
     object SignIn:Screen("signinscreen")
     object SignupScreen:Screen("signupscreen")
     object ChatRoomsScreen:Screen("chatroomscreen")
     object ChatScreen:Screen("chatscreen")
}