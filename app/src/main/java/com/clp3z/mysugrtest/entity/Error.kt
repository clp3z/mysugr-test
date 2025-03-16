package com.clp3z.mysugrtest.entity

sealed interface Error {
    data class Database(val message: String) : Error
    data class Unknown(val message: String) : Error
}

fun Error.toErrorMessage(): String = when (this) {
    is Error.Database -> message
    is Error.Unknown -> message
}
