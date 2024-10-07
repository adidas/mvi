---
title: 4. Examples
sidebar_position: 4
---

# Examples

This section provides examples of how to implement the adidas MVI library in your Android application.
For more detail check the sample provided.

## 1. Login Flow Example

Here is a complete example of how to implement a login flow using the adidas MVI library.

### ViewModel Implementation

The following code shows the `LoginViewModel`, which handles the login process.

```kotlin
class LoginViewModel(
    logger: Logger,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel(), MviHost<LoginIntent, State<LoginState, LoginSideEffect>> {

    private val reducer = Reducer(
        coroutineScope = viewModelScope,
        defaultDispatcher = coroutineDispatcher,
        initialInnerState = LoginState.LoggedOut(isLoggingIn = false),
        logger = logger,
        intentExecutor = this::executeIntent,
    )

    override val state = reducer.state

    override fun execute(intent: LoginIntent) {
        reducer.executeIntent(intent)
    }

    private fun executeIntent(intent: LoginIntent) =
        when (intent) {
            is LoginIntent.Login -> executeLogin(intent)
            LoginIntent.Logout -> executeLogout()
            LoginIntent.Close -> executeClose()
        }

    private fun executeLogin(intent: LoginIntent.Login) = flow {
        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = true))
        delay(300) // Simulate a network call
        emit(LoginTransform.SetIsLoggingIn(isLoggingIn = false))

        if (intent.username.isEmpty() || intent.password.isEmpty()) {
            emit(LoginTransform.AddSideEffect(LoginSideEffect.ShowInvalidCredentialsError))
        } else {
            emit(LoginTransform.SetLoggedIn(intent.username))
        }
    }
}
```

# Intent Example

Define the intents that your application will handle:

```kotlin
internal sealed class LoginIntent : Intent {
    data class Login(val username: String, val password: String) : LoginIntent()
    object Logout : LoginIntent()
    object Close : LoginIntent()
}
```

# Activity Implementation

Here's how to set up the `MviSampleActivity` to use the `LoginViewModel`.

```kotlin
class MviSampleActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModel() // Assuming you're using Koin for dependency injection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(viewModel)
        }
    }
}
```

# View Implementation

In your composable function, observe the state from the `LoginViewModel` and render the UI accordingly.

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is LoginState.LoggedOut -> {
            // Show login UI
        }
        is LoginState.LoggedIn -> {
            // Show logged-in UI
        }
    }
}
```