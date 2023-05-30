<div align="center">
  <img style='width: 500px' src="assets/mvi_logo.png" alt="adidas mvi Logo"/>
</div>

[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
![Test workflow](https://github.com/adidas/mvi/actions/workflows/deploy_docs.yml/badge.svg)
[![adidas official](https://img.shields.io/badge/adidas-official-000000)](https://github.com/adidas)

## Introduction

adidas MVI is an efficient library that helps you implement the MVI architectural pattern in your Android or JVM
applications. MVI is a pattern that promotes a unidirectional flow of data and provides a clear separation between the
different components of your application. This library aims to simplify the implementation of MVI by providing a set of
tools and utilities.

## Current Version

Here are the current available mvi modules versions:

| Module      |                                                                       Version                                                                        |
|-------------|:----------------------------------------------------------------------------------------------------------------------------------------------------:|
| mvi         |         [![Maven Central](https://img.shields.io/maven-central/v/com.adidas.mvi/mvi)](https://mvnrepository.com/artifact/com.adidas.mvi/mvi)         |
| mvi-compose | [![Maven Central](https://img.shields.io/maven-central/v/com.adidas.mvi/mvi-compose)](https://mvnrepository.com/artifact/com.adidas.mvi/mvi-compose) |

## Features

**Simple and lightweight**: The library is designed to be easy to understand and use, without unnecessary complexity or
overhead.

**Unidirectional data flow**: The MVI pattern ensures that data flows in a single direction, making it easier to reason
about and test your application.

**Separation of concerns**: The pattern encourages a clear separation between the model, view, and intent components of your
application.

**Immutable data**: The library promotes the use of immutable data structures, which helps reduce bugs and makes your code
more reliable.

**Thread safety**: Offers built-in mechanisms to handle concurrency and synchronization when managing the application's
state. This ensures that multiple threads can safely access and modify the state without encountering data races or
inconsistencies.

## Usage in adidas

MVI library is now being used in adidas CONFIRMED app. In fact, this library was developed initially as core
architecture of CONFIRMED app, and later it was decided to open source it.

## Installation

To use the adidas MVI Library in your Android project, follow these steps:

Open the build.gradle file of your project.
Add the following dependency to the dependencies block:

```
implementation("com.adidas.mvi:mvi:{LATEST_VERSION}")
```

Sync your project with the Gradle files.

## Usage

To use the adidas MVI Library in your application, you need to understand the basic concepts and components of the MVI
pattern.

## Concepts

**Model**: The model represents the state of your application. It's an immutable data structure that holds all the
information needed to render the UI.

**View**: The view is responsible for rendering the UI and reacting to user interactions. It observes the model and updates
the UI accordingly.

**Intent**: An intent represents a user action or an event that triggers a state change in the model. Intents are dispatched
to the model, which updates its state based on the intent received.

## Components

adidas MVI Library provides the following components to help you implement the MVI pattern:

**View State**: Represents the immutable model data that the view observes. It holds all the necessary information
required to render the user interface (UI). The ViewState is updated by the model based on the received intents and
reflects the current state of the application.

**Side Effect**: Represents an action or an event that occurs as a result of the state update. It encapsulates tasks
such as network requests, database operations, or other external interactions. Side effects are triggered by the model
and can be used to perform background operations or trigger additional state changes. This action needs to be executed
only once.

**State**: The State<ViewState, SideEffect> class combines the ViewState and SideEffect components to provide a
comprehensive representation of the application state within the MVI pattern.

**State Transform**: This interface helps with transforming from one state to another, working closely with Reducer, it
encapsulates the necessary information for the model to update its state. There are some helper functions such as
requireAndReduceState() for more convenient.

**Reducer**: Serves as a middleware or intermediary component between the all MVI concepts and UI. It is also
responsible for running the intents with a specific coroutine dispatcher, Cancellation and logging of events.

## License

[Apache License](LICENSE)
