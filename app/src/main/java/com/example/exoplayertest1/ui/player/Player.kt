package com.example.exoplayertest1.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

@Composable
fun PlayerScreen(){
    val context = LocalContext.current.applicationContext
    val player = ExoPlayer.Builder(context).build()

    val mediaItem = MediaItem.fromUri("")
    player.setMediaItem(mediaItem)
    player.prepare()
    player.play()
}