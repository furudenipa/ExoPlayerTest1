package com.example.exoplayertest1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import com.example.exoplayertest1.ui.theme.ExoPlayerTest1Theme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem.fromUri
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoPlayerTest1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExoPlayerComp()
                }
            }
        }
    }
}

@Composable
fun ExoPlayerComp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        val videoURL       = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        val context        = LocalContext.current.applicationContext
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        val exoPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(fromUri(videoURL))
                playWhenReady    = true
                prepare()
            }
        DisposableEffect(
            key1 = AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    StyledPlayerView(context).apply {

                        var resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        player = exoPlayer
                    }.sur
                }),
            effect = {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        ON_RESUME -> {
                            Log.e("LIFECYCLE", "resumed")
                            exoPlayer.play()
                        }
                        ON_PAUSE  -> {
                            Log.e("LIFECYCLE", "paused")
                            exoPlayer.stop()
                        }

                        else ->{}
                    }
                }
                val lifecycle = lifecycleOwner.value.lifecycle
                lifecycle.addObserver(observer)
                onDispose {
                    exoPlayer.release()
                    lifecycle.removeObserver(observer)
                }
            }
        )
    }
}