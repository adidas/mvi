package com.adidas.mvi

public interface Loggable {
    public val attributes: Map<String, String>
        get() = emptyMap()
}
