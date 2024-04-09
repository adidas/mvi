package com.adidas.mvi.sample.login.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.adidas.mvi.compose.MviScreen
import com.adidas.mvi.sample.login.viewmodel.LoginIntent
import com.adidas.mvi.sample.login.viewmodel.LoginSideEffect
import com.adidas.mvi.sample.login.viewmodel.LoginState
import com.adidas.mvi.sample.login.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
) {

    val context = LocalContext.current

    MviScreen(
        state = viewModel.state,
        onSideEffect = { sideEffect ->
            consumeSideEffect(
                sideEffect = sideEffect,
                context = context
            )
        },
        onBackPressed = {
            viewModel.execute(LoginIntent.Close)
        },
    ) { view ->
        LoginLandingContent(
            state = view,
            intentExecutor = viewModel::execute,
        )
    }
}


@Composable
private fun LoginLandingContent(
    state: LoginState,
    intentExecutor: (intent: LoginIntent) -> Unit,
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
    ) {

        when (state) {
            is LoginState.LoggedOut ->
                LoginLoadedView(
                    isLoggingIn = state.isLoggingIn,
                    intentExecutor = intentExecutor,
                )

            is LoginState.LoggedIn -> WelcomeView(username = state.username, intentExecutor)
        }
    }
}

@Composable
private fun LoginLoadedView(
    intentExecutor: (intent: LoginIntent) -> Unit,
    isLoggingIn: Boolean
) {

    if (isLoggingIn) {
        LoginLoadingView()
    } else {
        LoginInputView(intentExecutor)
    }
}

@Composable
private fun LoginInputView(intentExecutor: (intent: LoginIntent) -> Unit) {
    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.h5,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = { intentExecutor(LoginIntent.Login(username, password)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

@Composable
private fun LoginLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun WelcomeView(
    username: String,
    intentExecutor: (intent: LoginIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome $username!", modifier = Modifier.padding(16.dp))
        Button(onClick = { intentExecutor(LoginIntent.Logout) }) {
            Text(text = "logout")
        }
    }
}


internal fun consumeSideEffect(sideEffect: LoginSideEffect, context: Context) {

    when (sideEffect) {

        LoginSideEffect.ShowInvalidCredentialsError -> {
            Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }

        LoginSideEffect.Close -> (context as Activity).finish()
    }
}