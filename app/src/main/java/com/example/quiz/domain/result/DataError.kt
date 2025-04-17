package com.example.quiz.domain.result

sealed interface DataError : Error {
    enum class Network : DataError {

    }
}