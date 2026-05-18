package com.grama.angana.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.grama.angana.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val isLogin: Boolean = true,
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onName(v: String) = _uiState.update { it.copy(name = v) }
    fun onPhone(v: String) = _uiState.update { it.copy(phone = v) }
    fun onEmail(v: String) = _uiState.update { it.copy(email = v) }
    fun onPassword(v: String) = _uiState.update { it.copy(password = v) }
    fun toggleMode() = _uiState.update { it.copy(isLogin = !it.isLogin, error = null) }

    fun submit() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            val result = if (state.isLogin) {
                authRepository.loginWithEmail(state.email.trim(), state.password)
            } else {
                authRepository.registerWithEmail(state.name.trim(), state.email.trim(), state.phone.trim(), state.password)
            }
            _uiState.update { it.copy(loading = false, success = result.isSuccess, error = result.exceptionOrNull()?.message) }
        }
    }
}

private inline fun MutableStateFlow<AuthUiState>.update(block: (AuthUiState) -> AuthUiState) {
    value = block(value)
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, onSuccess: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.success) { if (state.success) onSuccess() }
    val lottie by rememberLottieComposition(LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_jcikwtux.json"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Grama-Angana", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            if (state.isLogin) "Welcome back! Book community halls with ease."
            else "Create an account to manage community events.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LottieAnimation(
                    composition = lottie,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(MaterialTheme.shapes.large)
                )

                TabRow(selectedTabIndex = if (state.isLogin) 0 else 1) {
                    Tab(selected = state.isLogin, onClick = { if (!state.isLogin) viewModel.toggleMode() }, text = { Text("Login") })
                    Tab(selected = !state.isLogin, onClick = { if (state.isLogin) viewModel.toggleMode() }, text = { Text("Register") })
                }

                if (!state.isLogin) {
                    OutlinedTextField(state.name, viewModel::onName, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(state.phone, viewModel::onPhone, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                }
                OutlinedTextField(state.email, viewModel::onEmail, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(state.password, viewModel::onPassword, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

                Button(onClick = viewModel::submit, enabled = !state.loading, modifier = Modifier.fillMaxWidth()) {
                    Text(if (state.isLogin) "Login" else "Register")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {}) { androidx.compose.material3.Icon(Icons.Outlined.Mail, contentDescription = null); Text(" Email") }
                    TextButton(onClick = {}) { androidx.compose.material3.Icon(Icons.Outlined.PhoneAndroid, contentDescription = null); Text(" Phone OTP") }
                }
            }
        }
    }
}
