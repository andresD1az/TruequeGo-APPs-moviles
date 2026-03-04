package com.example.truequego_apps_moviles.utils

class ValidatedField<T>(initialValue: T, val validator: (T) -> Boolean) {
    var value: T = initialValue
    val isValid: Boolean
        get() = validator(value)
}
