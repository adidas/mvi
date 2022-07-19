package com.adidas.mvi.reducer

import com.adidas.mvi.Intent
import com.adidas.mvi.UniqueIntent

internal sealed class TestIntent : Intent {
    object SimpleIntent : TestIntent()

    object Transform1Producer : TestIntent()

    object FailedTransformProducer : TestIntent()

    object AbelIntent : TestIntent()
    object CainIntent : TestIntent()

    data class UniqueTransformIntent(val id: Int) : TestIntent(), UniqueIntent
}
