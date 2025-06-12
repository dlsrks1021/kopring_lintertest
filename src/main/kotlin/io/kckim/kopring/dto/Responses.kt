package io.kckim.kopring.dto

data class GenericResponse<T>(
    val data: T? = null,
    val message: String,
)
