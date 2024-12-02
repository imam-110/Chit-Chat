package kiet.imam.chatkaro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kiet.imam.chatkaro.Injection
import kiet.imam.chatkaro.data.UserRepository
import kotlinx.coroutines.launch
import kiet.imam.chatkaro.data.Result

class AuthViewModel : ViewModel() {
    private val userRepository : UserRepository
    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }
     private val _authResult = MutableLiveData<Result<Boolean>>()
     val authResult: LiveData<Result<Boolean>> get() = _authResult

     fun signUp(email: String, password: String, firstName: String, lastName: String) {
         viewModelScope.launch {
             _authResult.value = userRepository.signUp(email, password,firstName, lastName)
         }
     }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            _authResult.value = userRepository.signIn(email , password)
        }
    }



}