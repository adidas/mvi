---
title: 2. Core Concepts
sidebar_position: 2
---

# Core Concepts

The **Model-View-Intent (MVI)** pattern in adidas MVI follows these principles:

## Model

The **Model** represents the state of the application. It’s immutable and stores all the information required to render the user interface.

```kotlin
sealed class LoginState {
    data class LoggedOut(val isLoggingIn: Boolean) : LoginState()
    data class LoggedIn(val username: String) : LoginState()
}
```

## View

The **View** observes the model and updates itself accordingly. It reacts to changes in the **Model** and displays the current state.

## Intent

An **Intent** represents a user action or an event that leads to a state change in the **Model**.

```kotlin
sealed class LoginIntent {
    data class Login(val username: String, val password: String) : LoginIntent()
    object Logout : LoginIntent()
}
```

## Side Effects

A **SideEffect** represents an external action triggered by a state change, such as network calls, database updates, or other asynchronous operations.

```kotlin
sealed class LoginSideEffect {
    object ShowInvalidCredentialsError : LoginSideEffect()
    object Close : LoginSideEffect()
}
```

## State

The **State** combines the **ViewState** and **SideEffect** components to provide a unified application state. It helps manage complex states within the app.