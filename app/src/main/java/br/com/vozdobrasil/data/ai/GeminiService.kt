package br.com.vozdobrasil.data.ai
import br.com.vozdobrasil.data.model.SpotRequest
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object GeminiService {
    private val client = OkHttpClient()
    fun generateSpotScript(req: SpotRequest, backendUrl: String?): String {
        if(backendUrl.isNullOrBlank()) return fallbackSpot(req)
        return try {
            val payload = JSONObject().put("estabelecimento", req.estabelecimento).put("produto", req.produto).put("promocao", req.promocao).put("preco", req.preco).put("endereco", req.endereco).put("telefone", req.telefone).put("estilo", req.estilo).put("duracao", req.duracaoSegundos)
            val request = Request.Builder().url("$backendUrl/api/gemini/spot").post(payload.toString().toRequestBody("application/json".toMediaType())).build()
            val resp = client.newCall(request).execute()
            if(resp.isSuccessful){ JSONObject(resp.body?.string() ?: "").optString("text", fallbackSpot(req)) } else fallbackSpot(req)
        } catch(e:Exception){ fallbackSpot(req) }
    }
    fun fallbackSpot(req: SpotRequest): String {
        val sb = StringBuilder()
        sb.append("ATENCAO, ${req.estabelecimento.uppercase()}! ")
        sb.append("${req.produto} com uma oferta que voce nao pode perder! ")
        sb.append(if(req.promocao.isNotBlank()) req.promocao + " " else "Qualidade e preco baixo de verdade. ")
        if(req.preco.isNotBlank()) sb.append("Apenas ${req.preco}. ")
        sb.append("Venha conferir, aproveite e garanta ja o seu! ")
        if(req.endereco.isNotBlank()) sb.append("Estamos em ${req.endereco}. ")
        if(req.telefone.isNotBlank()) sb.append("Peca pelo ${req.telefone}. ")
        sb.append("${req.estabelecimento} - aqui voce encontra o melhor!")
        return sb.toString()
    }
    fun parseCustomVoice(description: String): Map<String, Any> {
        val lower = description.lowercase()
        val gender = if(lower.contains("feminina") || lower.contains("mulher") || lower.contains("locutora")) "FEMININO" else "MASCULINO"
        val pitch = when {
            lower.contains("muito grave") || lower.contains("profunda") -> 0.72f
            lower.contains("grave") -> 0.85f
            lower.contains("aguda") || lower.contains("fina") -> 1.18f
            lower.contains("suave") -> 1.05f
            else -> 1.0f
        }
        val rate = when { lower.contains("rapida") || lower.contains("animada") || lower.contains("oferta") -> 1.12f; lower.contains("lenta") || lower.contains("serena") || lower.contains("padre") -> 0.92f; else -> 1.0f }
        val style = when {
            lower.contains("padre") -> "RELIGIOSO"; lower.contains("radio") -> "RADIO"; lower.contains("spot") -> "SPOT"; lower.contains("documentario") -> "CINEMA"; lower.contains("vendedor") || lower.contains("supermercado") -> "OFERTAS"; lower.contains("apresentador") -> "INSTITUCIONAL"; else -> "COMERCIAL"
        }
        return mapOf("gender" to gender, "pitch" to pitch, "rate" to rate, "style" to style)
    }
}
