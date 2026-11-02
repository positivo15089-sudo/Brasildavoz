package br.com.vozdobrasil.data.tts
import android.content.Context
import android.speech.tts.TextToSpeech
import br.com.vozdobrasil.data.model.VoiceProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

sealed interface TtsState { data object Idle: TtsState; data object Generating: TtsState; data class Ready(val file: File): TtsState; data class Error(val message: String): TtsState }

interface TtsProvider { suspend fun synthesize(text:String, voice:VoiceProfile, speed:Float, pitch:Float, outFile:File, backendUrl:String?): Result<File> }

@Singleton
class AndroidTtsProvider @Inject constructor(@ApplicationContext private val ctx: Context): TtsProvider {
    private var tts: TextToSpeech? = null
    override suspend fun synthesize(text:String, voice:VoiceProfile, speed:Float, pitch:Float, outFile:File, backendUrl:String?): Result<File> {
        return try {
            val ttsObj = TextToSpeech(ctx) { status ->
                if(status==TextToSpeech.SUCCESS){
                    ttsObj.language = Locale("pt","BR")
                    ttsObj.setSpeechRate(speed)
                    ttsObj.setPitch(pitch)
                    val ptVoices = ttsObj.voices?.filter{ it.locale.toString().startsWith("pt") }
                    if(!ptVoices.isNullOrEmpty()) ttsObj.voice = ptVoices.first()
                }
            }
            tts = ttsObj
            Thread.sleep(600)
            val result = ttsObj.synthesizeToFile(text, android.os.Bundle(), outFile, "vozbrasil-${System.currentTimeMillis()}")
            if(result==TextToSpeech.SUCCESS) Result.success(outFile) else Result.failure(Exception("Motor TTS retornou erro. Instale voz pt-BR em Configurações > Acessibilidade > Texto para voz."))
        } catch(e:Exception){ Result.failure(e) }
    }
    fun speakPreview(text:String, voice:VoiceProfile, speed:Float, pitch:Float){
        if(tts==null){ tts = TextToSpeech(ctx){ tts?.language=Locale("pt","BR") } }
        tts?.setSpeechRate(speed); tts?.setPitch(pitch); tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "preview")
    }
    fun stop(){ tts?.stop() }
}

@Singleton
class ElevenLabsProvider @Inject constructor(): TtsProvider {
    private val client = OkHttpClient()
    override suspend fun synthesize(text:String, voice:VoiceProfile, speed:Float, pitch:Float, outFile:File, backendUrl:String?): Result<File> {
        if(backendUrl.isNullOrBlank()) return Result.failure(Exception("ElevenLabs não configurado. Configure o Backend seguro em Configurações > API. O app usará o TTS nativo Android pt-BR até lá."))
        if(voice.providerVoiceId.isNullOrBlank()) return Result.failure(Exception("Esta voz não tem voice_id ElevenLabs mapeado. Use o TTS nativo ou cadastre o voice_id em Gerenciamento de API."))
        return try {
            val json = JSONObject().put("voiceId", voice.providerVoiceId).put("text", text).put("speed", speed)
            val req = Request.Builder().url("$backendUrl/api/tts/elevenlabs").post(json.toString().toRequestBody("application/json".toMediaType())).build()
            val resp = client.newCall(req).execute()
            if(!resp.isSuccessful) return Result.failure(Exception("Backend ElevenLabs: ${resp.code} ${resp.message}"))
            val bytes = resp.body?.bytes() ?: return Result.failure(Exception("Resposta vazia do backend"))
            FileOutputStream(outFile).use{ it.write(bytes) }
            Result.success(outFile)
        } catch(e:Exception){ Result.failure(e) }
    }
}

@Singleton
class VoiceEngine @Inject constructor(
    private val androidTts: AndroidTtsProvider,
    private val elevenLabs: ElevenLabsProvider
){
    val state = MutableStateFlow<TtsState>(TtsState.Idle)
    suspend fun generate(text:String, voice:VoiceProfile, speed:Float, pitch:Float, backendUrl:String?, useElevenLabs:Boolean, context: Context): Result<File> {
        state.value = TtsState.Generating
        val file = File(context.filesDir, "voz_${System.currentTimeMillis()}.mp3")
        val result = if(useElevenLabs && voice.provider=="ELEVENLABS") elevenLabs.synthesize(text, voice, speed, pitch, file, backendUrl) else androidTts.synthesize(text, voice, speed, pitch, file, backendUrl)
        result.onSuccess{ state.value = TtsState.Ready(it) }.onFailure{ state.value = TtsState.Error(it.message ?: "Erro desconhecido") }
        return result
    }
    fun preview(text:String, voice:VoiceProfile, speed:Float, pitch:Float){ androidTts.speakPreview(text.take(220), voice, speed, pitch) }
    fun stop(){ androidTts.stop() }
}
