package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse<T>(
    var isSuccess: Boolean,
    var data: T? = null,
    var error: String? = null
)
