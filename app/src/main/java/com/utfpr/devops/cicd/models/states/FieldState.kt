package com.utfpr.devops.cicd.models.states

data class FieldState(
    var value: String = "",
    var isError: Boolean = false,
    val minLength: Int,
    val maxLength: Int,
    val label: String
) {
    fun validate(): Boolean {
        isError = value.length < minLength || value.length > maxLength
        return !isError
    }
} 