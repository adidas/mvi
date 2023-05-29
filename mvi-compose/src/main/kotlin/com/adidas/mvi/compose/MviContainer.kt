package com.adidas.mvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adidas.mvi.State
import kotlinx.coroutines.flow.StateFlow

/**
 * The [MviContainer] is a composable function for implementing the MVI in Jetpack Compose, providing a convenient way to observe the state of your ViewModel.
 * The MVIContainer should be used in non-screen composable with a [com.adidas.confirmed.mvi.MviViewModel] attached, such as a ViewPager page that will be
 * included inside of a parent screen.
 * @param state The current state of the MVI ViewModel. This parameter must be of type [StateFlow]<[State]<TViewState, TSideEffect>>, where TViewState is the type
 * of the view state and TSideEffect is the type of the side effect.
 * @param onSideEffect A lambda function that is called when a side effect is emitted by the MVI ViewModel. The TSideEffect parameter of the function is
 * the emitted side effect.
 * @param onViewState A composable function that takes the current view state as its parameter and composes the UI for the container.
 */
@Composable
public fun <TViewState, TSideEffect> MviContainer(
    state: StateFlow<State<TViewState, TSideEffect>>,
    onSideEffect: (sideEffect: TSideEffect) -> Unit,
    onViewState: @Composable (viewState: TViewState) -> Unit,
) {
    val stateFlow by state.collectAsStateWithLifecycle()

    onViewState(stateFlow.view)

    SideEffect {
        stateFlow.sideEffects.forEach { sideEffect ->
            onSideEffect(sideEffect)
        }
    }
}
