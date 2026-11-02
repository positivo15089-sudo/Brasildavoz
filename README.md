# VOZ DO BRASIL — Estúdio Profissional de Locuções e SPOTs com IA

App Android completo (Kotlin + Jetpack Compose + MVVM + Hilt + Room) com:
- 20 locutores reais (10M / 10F) mapeados para TTS Android pt-BR + ElevenLabs
- Estúdio de Voz, Criar Minha Voz, Gerador de SPOTs, Estúdio de Áudio, Editor, Projetos
- TTS real: Android TextToSpeech pt-BR (funciona sem API) + ElevenLabs via backend seguro
- Gemini para roteiros de SPOT via backend seguro
- NENHUMA chave de API no app. Chaves só no backend.

## Como abrir e rodar
1. Abra em Android Studio. Sync Gradle.
2. Conecte celular Android ou emulador API 26+.
3. Run > app. APK em app/build/outputs/apk/debug/app-debug.apk (Build > Build APKs).

## Backend seguro (obrigatório para IA avançada)
cd backend
cp .env.example .env  # preencha ELEVENLABS_API_KEY e GEMINI_API_KEY
npm install
npm start
No app: Configurações > Gerenciamento de API > URL do Backend

## Sem backend
O app funciona 100% com TTS nativo Android pt-BR. Botões Gerar / Preview usam VoiceEngine real.
ElevenLabs e Gemini mostram aviso claro: precisa conectar backend — não simulam.

## Estrutura
- data/model/Models.kt — VoiceLibrary 20 vozes
- data/tts/TtsEngine.kt — AndroidTtsProvider + ElevenLabsProvider + VoiceEngine
- data/ai/GeminiService.kt — roteiro SPOT + parseCustomVoice
- data/audio/AudioStudio.kt — mixagem voz/música com ducking (música max 35-50%)
- MainActivity.kt — Splash, Home com 4 botões, 14 telas
- backend/server.js — proxy seguro ElevenLabs + Gemini

## APK
Build via Android Studio. Projeto pronto para Build APKs.
