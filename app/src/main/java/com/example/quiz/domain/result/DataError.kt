package com.example.quiz.domain.result

sealed interface DataError : Error {
    enum class Network : DataError {
        NoInternet,
        RequestTimeout,

        InvalidDataFormat,
        NoResults,
        RateLimit,

        Unknown
    }
}