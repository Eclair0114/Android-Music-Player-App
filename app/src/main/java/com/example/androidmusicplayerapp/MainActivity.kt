package com.example.androidmusicplayerapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var buttonPlay: Button
    private lateinit var seekBar: SeekBar
    private lateinit var textViewCurrentTime: TextView
    private lateinit var textViewTotalTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI要素の初期化
        buttonPlay = findViewById(R.id.buttonPlay)
        seekBar = findViewById(R.id.seekBar)
        textViewCurrentTime = findViewById(R.id.textViewCurrentTime)
        textViewTotalTime = findViewById(R.id.textViewTotalTime)

        // MediaPlayerの初期化
        // アセットフォルダ内の音楽ファイルを使用
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music)

        // 総再生時間を表示
        val totalDuration = mediaPlayer?.duration ?: 0
        textViewTotalTime.text = formatTime(totalDuration)

        // SeekBarの最大値を設定
        seekBar.max = totalDuration

        // 再生/一時停止ボタンのクリックリスナー
        buttonPlay.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        // SeekBarの変更リスナー
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                TODO("Not yet implemented")
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
                textViewCurrentTime.text = formatTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                TODO("Not yet implemented")
                // 何もしない
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                TODO("Not yet implemented")
                // 何もしない
            }
        })

        // 定期的にSeekBarを更新
        updateSeekBar()
    }

    private fun playMusic() {
        mediaPlayer?.start()
        buttonPlay.text = "一時停止"
        isPlaying = true
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        buttonPlay.text = "再生"
        isPlaying = false
    }

    private fun updateSeekBar() {
        val runnable = object : Runnable {
            override fun run() {
//                TODO("Not yet implemented")
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        val currentPosition = it.currentPosition
                        seekBar.progress = currentPosition
                        textViewCurrentTime.text = formatTime(currentPosition)
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) - TimeUnit.MINUTES.toSeconds(
                minutes
            )
        return String().format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}