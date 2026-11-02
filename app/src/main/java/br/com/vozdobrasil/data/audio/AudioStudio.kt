package br.com.vozdobrasil.data.audio
import android.content.Context
import android.media.MediaPlayer
import java.io.File

class AudioStudio(private val context: Context){
    var voicePlayer: MediaPlayer? = null
    var musicPlayer: MediaPlayer? = null
    var voiceVolume = 1.0f
    var musicVolume = 0.35f
    fun playVoice(file: File, onDone: ()->Unit = {}) {
        stopAll()
        voicePlayer = MediaPlayer().apply{ setDataSource(file.absolutePath); setVolume(voiceVolume, voiceVolume); setOnCompletionListener{ onDone() }; prepare(); start() }
    }
    fun playMusic(file: File, loop: Boolean = true){
        musicPlayer?.release()
        musicPlayer = MediaPlayer().apply{ setDataSource(file.absolutePath); setVolume(musicVolume, musicVolume); isLooping=loop; prepare(); start() }
    }
    fun playMixed(voice: File, music: File?){
        playVoice(voice)
        if(music!=null) playMusic(music)
    }
    fun setVolumes(v: Float, m: Float){ voiceVolume=v; musicVolume=m.coerceAtMost(0.5f); voicePlayer?.setVolume(v,v); musicPlayer?.setVolume(musicVolume, musicVolume) }
    fun stopAll(){ voicePlayer?.stop(); voicePlayer?.release(); voicePlayer=null; musicPlayer?.stop(); musicPlayer?.release(); musicPlayer=null }
    fun pause(){ voicePlayer?.pause(); musicPlayer?.pause() }
    fun resume(){ voicePlayer?.start(); musicPlayer?.start() }
}
