package com.adidas.mvi.reducer.logger

import io.kotest.matchers.string.shouldContainInOrder

infix fun String?.shouldContainSuccessfulIntent(substr: String): String? {
    this.shouldContainInOrder(SUCCESSFUL_INTENT, substr)
    return this
}

infix fun String?.shouldContainFailingIntent(substr: String): String? {
    this.shouldContainInOrder(FAILED_INTENT, substr)
    return this
}

infix fun String?.shouldContainSuccessfulTransform(substr: String): String? {
    this.shouldContainInOrder(SUCCESSFUL_TRANSFORM, substr)
    return this
}

infix fun String?.shouldContainFailingTransform(substr: String): String? {
    this.shouldContainInOrder(FAILED_TRANSFORM, substr)
    return this
}
