---
title: 3. Getting started
sidebar_position: 3
---

# Getting Started

To begin using adidas MVI in your project, follow these steps:

## 1. Add the Dependency

Add the following to your `build.gradle` file:

```gradle
implementation("com.adidas.mvi:mvi:{LATEST_VERSION}")
```

Sync your project to download the necessary dependencies.

## 2. Create the Components

Here's an example of setting up the **LoginViewModel** and managing intents.

### Login ViewModel

The **LoginViewModel** manages user actions and updates the application state based on intents.

```kotlin
class LoginViewModel( logger: Logger ) : ViewModel(), MviHost<LoginIntent, State<LoginState, LoginSideEffect>> {
    private val reducer = Reducer(
        coroutineScope = viewModelScope,
        initialInnerState = LoginState.LoggedOut(isLoggingIn = false),
        logger = logger,
        intentExecutor = this::executeIntent
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
}
```

## 3. Update the Activity

In your `Activity`, use the view model and set the content:

```kotlin
class MviSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}
```

Now you're ready to use adidas MVI in your project!
