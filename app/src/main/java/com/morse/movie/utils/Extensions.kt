package com.morse.movie.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.morse.movie.data.entities.remote.DetailsResponse
import com.morse.movie.data.entities.ui.State
import com.morse.movie.data.repository.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response

fun CoroutineScope.execute(suspendFunction: suspend () -> Unit) {
    launch {
        suspendFunction.invoke()
    }
}

fun Modifier.customBackground(isSelected: Boolean) = then(backgroundModifier(isSelected))

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.backgroundModifier(isSelected: Boolean) = if (isSelected) {
    Modifier.background(
        Brush.horizontalGradient(
            listOf(
                Color(0xFFF99F00),
                Color(0XFFDB3069)
            )
        ),
        CircleShape
    )
} else {
    Modifier.background(Color.White)
}

fun String.log() {
    Log.i("Mao-Trailer", this)
}

fun Context.shareMedia(details: DetailsResponse) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Watch ${details.title} 👋")
        putExtra(Intent.EXTRA_TEXT, details.overview)
    }
    startActivity(
        Intent.createChooser(shareIntent, "Share With")
    )
}

fun Context.openYoutubeTrailer(id: Int) {

}


@Composable
fun ExecuteFromVM(key: Any, execute: () -> Unit) {
    LaunchedEffect(key) {
        execute.invoke()
    }
}


fun <Result, MapResult> BaseRepository.execute(
    executionApi: suspend () -> Response<Result>,
    mapFunctions: (Result?) -> MapResult
) = flow {
    val result = executionApi.invoke()
    if (result.isSuccessful) {
        emit(mapFunctions.invoke(result.body()))
    } else {
        println("Error , Can`t Execute the Api Because ${result.errorBody()?.string()}")
    }
}

fun <Result, MapResult> BaseRepository.executeState(
    executionApi: suspend () -> Response<Result>,
    mapFunctions: (Result?) -> MapResult
) = flow {
    val result = executionApi.invoke()
    if (result.isSuccessful) {
        emit(State.Success(mapFunctions.invoke(result.body())))
    } else {
        emit(State.Error(result.errorBody()?.string() ?: "Fail to load this request ."))
    }
}

fun <Result> BaseRepository.executeState(
    executionApi: suspend () -> Response<Result>
) = flow {
    val result = executionApi.invoke()
    if (result.isSuccessful) {
        emit(State.Success(result.body()))
    } else {
        emit(State.Error(result.errorBody()?.string() ?: "Fail to load this request ."))
    }
}