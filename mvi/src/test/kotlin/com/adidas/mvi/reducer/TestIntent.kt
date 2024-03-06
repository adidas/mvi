package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.UniqueIntent

internal sealed class TestIntent : Intent {
    data object SimpleIntent : TestIntent()

    data object Transform1Producer : TestIntent()

    data object FailedTransformProducer : TestIntent()

    data object AbelIntent : TestIntent()

    data object CainIntent : TestIntent()

    data class UniqueTransformIntent(val id: Int) : TestIntent(), UniqueIntent
}
