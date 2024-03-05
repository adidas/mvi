package com.adidas.mvi.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.adidas.mvi.State
import kotlinx.coroutines.flow.StateFlow

/**
 * The [MviScreen] is a composable function for implementing the MVI Jetpack Compose, providing a convenient way to observe the state of your ViewModel
 * inside a screen composable.
 * @param state The current state of the ViewModel or similar component. This parameter must be of type [StateFlow]<[State]<TViewState, TSideEffect>>, where TViewState is the type
 * of the view state and TSideEffect is the type of the side effect.
 * @param onSideEffect A lambda function that is called when a side effect is emitted by the MVI architecture. The TSideEffect parameter of the function
 * is the emitted side effect.
 * @param onBackPressed A lambda function that is called when the back button is pressed.
 * @param onViewState A composable function that takes the current view state as its parameter and composes the UI for the screen.
 * @note The [MviScreen] should be used to implement a screen using the MVI architecture. It is a convenience function that combines the MVIContainer
 * composable with a [BackHandler] that handles the back button press. However, if you need to use the MVI architecture in a non-screen composable, such as
 * a ViewPager page, you should use the [MviContainer] composable instead.
 */
@Composable
public fun <TViewState, TSideEffect> MviScreen(
    state: StateFlow<State<TViewState, TSideEffect>>,
    onSideEffect: (sideEffect: TSideEffect) -> Unit,
    onBackPressed: () -> Unit,
    onViewState: @Composable (viewState: TViewState) -> Unit,
) {
    MviContainer(
        state = state,
        onSideEffect = onSideEffect,
        onViewState = onViewState,
    )

    BackHandler {
        onBackPressed()
    }
}
