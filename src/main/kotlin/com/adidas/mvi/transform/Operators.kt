package com.adidas.mvi.transform

public fun <TOwner> notOperator(function: (TOwner) -> Boolean): (TOwner) -> Boolean {
    return {
        !function(it)
    }
}

public fun <TOwner, TValue> equalsOperator(retriever: (TOwner) -> TValue, valueToCompare: TValue): (TOwner) -> Boolean {
    return {
        retriever(it) == valueToCompare
    }
}

public fun <TOwner, TValue> notEqualsOperator(retriever: (TOwner) -> TValue, valueToCompare: TValue): (TOwner) -> Boolean {
    return notOperator(equalsOperator(retriever, valueToCompare))
}
