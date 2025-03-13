package com.clp3z.mysugrtest.entity

import android.net.http.HttpException
import androidx.room.Room

sealed interface Error {
    data class Database(val message: String) : Error
    data class Unknown(val message: String) : Error
}

