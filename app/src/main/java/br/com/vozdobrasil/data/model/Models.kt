package br.com.vozdobrasil.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class VoiceProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val gender: String,
    val category: String,
    val pitch: Float = 1.0f,
    val rate: Float = 1.0f,
    val providerVoiceId: String? = null,
    val provider: String = "ANDROID_TTS",
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val locale: String = "pt-BR"
)

@Entity(tableName="projects")
data class Project(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val voiceName: String,
    val voiceId: String,
    val type: String = "LOCUCAO",
    val audioPath: String? = null,
    val durationMs: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val folder: String = "Geral",
    val isFavorite: Boolean = false,
    val spotStyle: String? = null
)

data class SpotRequest(
    val estabelecimento: String,
    val produto: String,
    val promocao: String = "",
    val preco: String = "",
    val endereco: String = "",
    val telefone: String = "",
    val estilo: String = "SPOT comercial de rádio",
    val duracaoSegundos: Int = 30
)

object VoiceLibrary {
    fun all(): List<VoiceProfile> = listOf(
        VoiceProfile(name="Locutor Comercial Premium", description="Voz grave, encorpada e impactante.", gender="MASCULINO", category="COMERCIAL", pitch=0.82f, rate=0.98f, providerVoiceId="pNInz6obpgDQGcFmaJgB", provider="ELEVENLABS"),
        VoiceProfile(name="Locutor de SPOT", description="Voz muito grave, potente e profissional.", gender="MASCULINO", category="SPOT", pitch=0.75f, rate=1.0f, providerVoiceId="ErXwobaYiN019PkySvjV", provider="ELEVENLABS"),
        VoiceProfile(name="Locutor de Rádio", description="Estilo radialista brasileiro clássico.", gender="MASCULINO", category="RADIO", pitch=0.9f, rate=1.05f),
        VoiceProfile(name="Locutor de Ofertas", description="Voz animada e persuasiva para varejo.", gender="MASCULINO", category="OFERTAS", pitch=1.0f, rate=1.12f),
        VoiceProfile(name="Locutor Cinematográfico", description="Voz profunda e dramática.", gender="MASCULINO", category="CINEMA", pitch=0.78f, rate=0.92f),
        VoiceProfile(name="Locutor Institucional", description="Voz séria e elegante.", gender="MASCULINO", category="INSTITUCIONAL", pitch=0.88f, rate=0.98f),
        VoiceProfile(name="Locutor Jovem", description="Moderna e descontraída.", gender="MASCULINO", category="JOVEM", pitch=1.05f, rate=1.08f),
        VoiceProfile(name="Locutor Sertanejo", description="Estilo comercial de rodeio e sertanejo.", gender="MASCULINO", category="SERTANEJO", pitch=0.92f, rate=1.05f),
        VoiceProfile(name="Narrador Esportivo", description="Energética e vibrante.", gender="MASCULINO", category="ESPORTE", pitch=1.0f, rate=1.18f),
        VoiceProfile(name="Narrador Religioso", description="Serena e expressiva.", gender="MASCULINO", category="RELIGIOSO", pitch=0.9f, rate=0.94f),
        VoiceProfile(name="Locutora Comercial Premium", description="Voz feminina premium, clara e impactante.", gender="FEMININO", category="COMERCIAL", pitch=1.0f, rate=0.98f, providerVoiceId="21m00Tcm4TlvDq8ikWAM", provider="ELEVENLABS"),
        VoiceProfile(name="Locutora de Rádio", description="Radialista feminina brasileira.", gender="FEMININO", category="RADIO", pitch=1.05f, rate=1.04f),
        VoiceProfile(name="Locutora de SPOT", description="Potente para SPOTs de varejo.", gender="FEMININO", category="SPOT", pitch=0.98f, rate=1.06f),
        VoiceProfile(name="Locutora Institucional", description="Séria, elegante e confiável.", gender="FEMININO", category="INSTITUCIONAL", pitch=0.95f, rate=0.97f),
        VoiceProfile(name="Locutora Jovem", description="Jovem, leve e atual.", gender="FEMININO", category="JOVEM", pitch=1.15f, rate=1.1f),
        VoiceProfile(name="Locutora Elegante", description="Sofisticada, ideal para marcas premium.", gender="FEMININO", category="ELEGANTE", pitch=0.98f, rate=0.96f),
        VoiceProfile(name="Locutora Emocional", description="Calorosa e envolvente.", gender="FEMININO", category="EMOCIONAL", pitch=1.02f, rate=0.98f),
        VoiceProfile(name="Narradora Cinematográfica", description="Profunda, narrativa de documentário.", gender="FEMININO", category="CINEMA", pitch=0.92f, rate=0.94f),
        VoiceProfile(name="Locutora de Ofertas", description="Animada, vendedora, promocional.", gender="FEMININO", category="OFERTAS", pitch=1.08f, rate=1.14f),
        VoiceProfile(name="Voz feminina suave", description="Suave, acolhedora e calma.", gender="FEMININO", category="SUAVE", pitch=1.1f, rate=0.94f)
    )
}
