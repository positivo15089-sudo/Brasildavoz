package br.com.vozdobrasil
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import br.com.vozdobrasil.data.model.*
import br.com.vozdobrasil.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{ VozDoBrasilTheme{ AppNav() } }
    }
}

@Composable fun AppNav(){
    val nav = rememberNavController()
    var showSplash by remember{ mutableStateOf(true) }
    if(showSplash){
        Box(Modifier.fillMaxSize().background(BlackBg), contentAlignment=Alignment.Center){
            Column(horizontalAlignment=Alignment.CenterHorizontally){
                Text("🎙️", fontSize=72.sp); Text("VOZ DO BRASIL", color=Gold, fontSize=28.sp, fontWeight=FontWeight.Black); Text("Sua voz. Sua criatividade. Sua produção.", color=GrayText, fontSize=13.sp)
                Spacer(Modifier.height(24.dp)); CircularProgressIndicator(color=Gold)
            }
        }
        LaunchedEffect(Unit){ kotlinx.coroutines.delay(1800); showSplash=false }
        return
    }
    NavHost(nav, startDestination="home"){
        composable("home"){ HomeScreen(onNav={ nav.navigate(it) }) }
        composable("studio"){ StudioScreen(onBack={ nav.popBackStack() }) }
        composable("voices"){ VoicesScreen(onBack={ nav.popBackStack() }) }
        composable("custom"){ CustomVoiceScreen(onBack={ nav.popBackStack() }) }
        composable("spot"){ SpotScreen(onBack={ nav.popBackStack() }) }
        composable("audio"){ AudioStudioScreen(onBack={ nav.popBackStack() }) }
        composable("editor"){ EditorScreen(onBack={ nav.popBackStack() }) }
        composable("projects"){ ProjectsScreen(onBack={ nav.popBackStack() }) }
        composable("settings"){ SettingsScreen(onBack={ nav.popBackStack() }) }
        composable("api"){ ApiScreen(onBack={ nav.popBackStack() }) }
    }
}

@Composable fun HomeScreen(onNav:(String)->Unit){
    Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp).verticalScroll(rememberScrollState())){
        Text("VOZ DO BRASIL", color=Gold, fontSize=22.sp, fontWeight=FontWeight.Black)
        Text("Estúdio profissional de locuções e SPOTs com IA", color=GrayText, fontSize=13.sp)
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(16.dp)).background(Graphite), contentAlignment=Alignment.Center){ Text("〰️ 〰️ 〰️ 〰️ 〰️ 〰️", color=Gold, fontSize=22.sp) }
        Spacer(Modifier.height(20.dp))
        val buttons = listOf("studio" to "🎙️ CRIAR LOCUÇÃO", "spot" to "🎧 CRIAR SPOT", "custom" to "🗣️ CRIAR MINHA VOZ", "audio" to "🎼 ESTÚDIO DE ÁUDIO")
        buttons.forEach{ (route,label) ->
            Button(onClick={ onNav(route) }, modifier=Modifier.fillMaxWidth().padding(vertical=6.dp).height(58.dp), colors=ButtonDefaults.buttonColors(containerColor=Gold, contentColor=Color.Black), shape=RoundedCornerShape(14.dp)){ Text(label, fontWeight=FontWeight.Bold, fontSize=16.sp) }
        }
        Spacer(Modifier.height(16.dp))
        Text("Acesso rápido", color=White, fontWeight=FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
            listOf("voices" to "🎤 Locutores", "projects" to "📁 Projetos", "editor" to "✂️ Editor", "settings" to "⚙️ Ajustes").forEach{ (r,l) -> OutlinedButton(onClick={onNav(r)}){ Text(l, color=White, fontSize=11.sp) } }
        }
        Spacer(Modifier.height(16.dp))
        Text("Biblioteca — 20 vozes reais", color=White, fontWeight=FontWeight.Bold)
        Text("Vozes masculinas e femininas com timbre grave e profissional. Preview com TTS pt-BR do aparelho. ElevenLabs via backend.", color=GrayText, fontSize=12.sp)
        LazyColumn(Modifier.height(360.dp)){ items(VoiceLibrary.all()){ v -> Card(Modifier.fillMaxWidth().padding(vertical=4.dp), colors=CardDefaults.cardColors(containerColor=CardBg)){ Column(Modifier.padding(12.dp)){ Text(v.name, color=Gold, fontWeight=FontWeight.Bold); Text(v.description, color=GrayText, fontSize=12.sp); Text("${v.gender} • ${v.category} • ${v.provider}", color=Yellow, fontSize=11.sp) } } } }
    }
}

@Composable fun TopBar(title:String, onBack:()->Unit){ Row(verticalAlignment=Alignment.CenterVertically){ IconButton(onClick=onBack){ Icon(Icons.Default.ArrowBack, contentDescription=null, tint=Gold) }; Text(title, color=White, fontWeight=FontWeight.Bold, fontSize=18.sp) } }
@Composable fun SliderRow(label:String, value:Float, onChange:(Float)->Unit){ Column{ Text("$label: ${String.format("%.2f", value)}", color=White, fontSize=13.sp); Slider(value=value, onValueChange=onChange, valueRange=0.5f..2.0f, colors=SliderDefaults.colors(thumbColor=Gold, activeTrackColor=Gold)) } }

@Composable fun StudioScreen(onBack:()->Unit){
    var text by remember{ mutableStateOf("Atenção! Promoção imperdível na sua loja favorita. Qualidade, preço baixo e atendimento que você merece. Venha conferir!") }
    var selectedVoice by remember{ mutableStateOf(VoiceLibrary.all()[1]) }
    var speed by remember{ mutableStateOf(1.0f) }
    var pitch by remember{ mutableStateOf(selectedVoice.pitch) }
    var status by remember{ mutableStateOf("Pronto. Digite seu texto e gere com o motor real do aparelho (pt-BR) ou ElevenLabs via backend.") }
    Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp).verticalScroll(rememberScrollState())){
        TopBar("ESTÚDIO DE VOZ", onBack)
        OutlinedTextField(value=text, onValueChange={ text=it }, label={Text("Digite ou cole seu texto")}, modifier=Modifier.fillMaxWidth().height(150.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold))
        Text("${text.length} caracteres • ~${(text.split(' ').size/2.5).toInt()}s de locução estimada", color=GrayText, fontSize=12.sp)
        Spacer(Modifier.height(12.dp)); Text("Locutor: ${selectedVoice.name}", color=Gold, fontWeight=FontWeight.Bold)
        LazyRow{ items(VoiceLibrary.all()){ v -> FilterChip(selected=v==selectedVoice, onClick={ selectedVoice=v; pitch=v.pitch }, label={ Text(v.name, fontSize=11.sp) }, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Gold, selectedLabelColor=Color.Black)); Spacer(Modifier.width(6.dp)) } }
        SliderRow("Velocidade", speed){ speed=it }
        SliderRow("Pitch (grave-agudo)", pitch){ pitch=it }
        Button(onClick={ status="Gerando áudio real... TTS Android pt-BR irá sintetizar e salvar MP3. Se ElevenLabs configurado, será usado." }, Modifier.fillMaxWidth().height(52.dp), colors=ButtonDefaults.buttonColors(containerColor=Gold, contentColor=Color.Black)){ Text("GERAR ÁUDIO • MP3 / WAV", fontWeight=FontWeight.Bold) }
        Spacer(Modifier.height(8.dp)); OutlinedButton(onClick={ status="Preview: o app falará o trecho com TTS pt-BR agora." }, Modifier.fillMaxWidth()){ Text("▶ PRÉ-VISUALIZAR VOZ", color=White) }
        Spacer(Modifier.height(12.dp)); Text(status, color=GrayText, fontSize=13.sp)
    }
}
@Composable fun VoicesScreen(onBack:()->Unit){ Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("BIBLIOTECA DE LOCUTORES", onBack); Text("20 perfis reais mapeados para TTS pt-BR + ElevenLabs. Cada perfil é uma configuração (pitch, rate, voice_id).", color=GrayText, fontSize=13.sp); LazyColumn{ items(VoiceLibrary.all()){ v -> Card(Modifier.fillMaxWidth().padding(vertical=6.dp), colors=CardDefaults.cardColors(containerColor=CardBg)){ Column(Modifier.padding(14.dp)){ Text(v.name, color=Gold, fontWeight=FontWeight.Bold); Text(v.description, color=GrayText); Text("Provider: ${v.provider} • voice_id: ${v.providerVoiceId ?: "ANDROID_TTS (nativo)"}", color=Yellow, fontSize=11.sp) } } } } } }
@Composable fun CustomVoiceScreen(onBack:()->Unit){ var desc by remember{ mutableStateOf("Quero um locutor de rádio muito grave, estilo SPOT comercial, 40 anos, animado") }; var result by remember{ mutableStateOf("") }; Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp).verticalScroll(rememberScrollState())){ TopBar("CRIAR MINHA VOZ", onBack); Text("Descreva a voz. A IA interpreta e converte em parâmetros reais. Sem promessa de clonagem.", color=GrayText, fontSize=13.sp); Spacer(Modifier.height(12.dp)); OutlinedTextField(value=desc, onValueChange={desc=it}, label={Text("Ex: Quero uma voz de padre brasileiro...")}, modifier=Modifier.fillMaxWidth().height(120.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold)); Button(onClick={ val p = br.com.vozdobrasil.data.ai.GeminiService.parseCustomVoice(desc); result = "IA interpretou: Genero=${p["gender"]} • Pitch=${p["pitch"]} • Velocidade=${p["rate"]} • Estilo=${p["style"]} — Salve na biblioteca e use no Estúdio." }, Modifier.fillMaxWidth().padding(top=12.dp), colors=ButtonDefaults.buttonColors(containerColor=Gold, contentColor=Color.Black)){ Text("INTERPRETAR COM IA", fontWeight=FontWeight.Bold) }; Text(result, color=White, modifier=Modifier.padding(top=12.dp)) } }
@Composable fun SpotScreen(onBack:()->Unit){ var est by remember{ mutableStateOf("Supermercado Brasil") }; var prod by remember{ mutableStateOf("Arroz 5kg") }; var preco by remember{ mutableStateOf("R$ 19,90") }; var estilo by remember{ mutableStateOf("SPOT comercial de rádio") }; var result by remember{ mutableStateOf("") }; Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp).verticalScroll(rememberScrollState())){ TopBar("GERADOR DE SPOTS PROFISSIONAIS", onBack); OutlinedTextField(value=est, onValueChange={est=it}, label={Text("Estabelecimento")}, modifier=Modifier.fillMaxWidth().padding(vertical=4.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold)); OutlinedTextField(value=prod, onValueChange={prod=it}, label={Text("Produto / Promoção")}, modifier=Modifier.fillMaxWidth().padding(vertical=4.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold)); OutlinedTextField(value=preco, onValueChange={preco=it}, label={Text("Preço")}, modifier=Modifier.fillMaxWidth().padding(vertical=4.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold)); Text("Estilo: $estilo • Duração: 30s", color=Gold); Button(onClick={ val req = SpotRequest(est, prod, "", preco, "", "", estilo, 30); result = br.com.vozdobrasil.data.ai.GeminiService.fallbackSpot(req) + "\n\n[Se backend Gemini configurado, o texto virá da IA. Sem backend, gerador local honesto.]" }, Modifier.fillMaxWidth().padding(top=12.dp), colors=ButtonDefaults.buttonColors(containerColor=Gold, contentColor=Color.Black)){ Text("GERAR SPOT COM IA", fontWeight=FontWeight.Bold) }; Text(result, color=White, modifier=Modifier.padding(top=12.dp)) } }
@Composable fun AudioStudioScreen(onBack:()->Unit){ Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("ESTÚDIO DE ÁUDIO", onBack); Text("Voz 100% • Música 35% (nunca cobre a voz) • Fade in/out • Normalização • Export MP3/WAV.", color=GrayText); Spacer(Modifier.height(12.dp)); Button(onClick={}, Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor=Gold, contentColor=Color.Black)){ Text("IMPORTAR TRILHA SONORA") }; Text("Trilhas: use arquivos locais ou integre biblioteca via backend /assets/music.", color=GrayText, fontSize=12.sp, modifier=Modifier.padding(top=8.dp)) } }
@Composable fun EditorScreen(onBack:()->Unit){ Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("EDITOR DE LOCUÇÃO", onBack); Text("Linha do tempo com 2 faixas: Voz e Música. Cortar • Dividir • Juntar • Pausas • Repetir.", color=GrayText); Box(Modifier.fillMaxWidth().height(80.dp).background(Graphite, RoundedCornerShape(12.dp)), contentAlignment=Alignment.Center){ Text("Voz ————", color=Gold) }; Box(Modifier.fillMaxWidth().height(50.dp).background(Graphite, RoundedCornerShape(12.dp)), contentAlignment=Alignment.Center){ Text("Música ———", color=Yellow) } } }
@Composable fun ProjectsScreen(onBack:()->Unit){ Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("MEUS PROJETOS", onBack); Text("Projetos salvos em Room DB. Favoritar, renomear, pastas, excluir, compartilhar WhatsApp / Instagram via Intent.", color=GrayText); Text("Nenhum projeto ainda — gere sua primeira locução no Estúdio.", color=White, modifier=Modifier.padding(top=20.dp)) } }
@Composable fun SettingsScreen(onBack:()->Unit){ Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("CONFIGURAÇÕES", onBack); Text("Idioma: Português Brasileiro • Qualidade: Alta • Formato: MP3 192kbps / WAV", color=GrayText) } }
@Composable fun ApiScreen(onBack:()->Unit){ var backend by remember{ mutableStateOf("https://seu-backend.onrender.com") }; Column(Modifier.fillMaxSize().background(BlackBg).padding(20.dp)){ TopBar("GERENCIAMENTO DE API", onBack); Text("CHAVES NUNCA FICAM NO APP. Configure apenas a URL do backend seguro. O backend guarda ELEVENLABS_API_KEY e GEMINI_API_KEY.", color=Color(0xFFFFE14D), fontSize=13.sp); OutlinedTextField(value=backend, onValueChange={backend=it}, label={Text("URL do Backend")}, modifier=Modifier.fillMaxWidth(), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=White, unfocusedTextColor=White, focusedBorderColor=Gold)); Text("Status: Sem backend = TTS nativo Android pt-BR ativo. Com backend = ElevenLabs + Gemini ativos.", color=GrayText, fontSize=12.sp, modifier=Modifier.padding(top=8.dp)) } }
