require('dotenv').config();
const express=require('express'); const cors=require('cors');
const app=express(); app.use(cors()); app.use(express.json({limit:'2mb'}));
const ELEVEN=process.env.ELEVENLABS_API_KEY; const GEMINI=process.env.GEMINI_API_KEY;
app.post('/api/tts/elevenlabs', async(req,res)=>{
  try{
    const {voiceId,text}=req.body;
    if(!ELEVEN) return res.status(500).json({error:'ELEVENLABS_API_KEY nao configurada no backend'});
    const r=await fetch('https://api.elevenlabs.io/v1/text-to-speech/'+voiceId,{method:'POST',headers:{'xi-api-key':ELEVEN,'Content-Type':'application/json'},body:JSON.stringify({text, model_id:'eleven_multilingual_v2', voice_settings:{stability:0.5, similarity_boost:0.75, style:0.3, use_speaker_boost:true}})});
    if(!r.ok) return res.status(r.status).json({error:await r.text()});
    const buf=Buffer.from(await r.arrayBuffer()); res.set('Content-Type','audio/mpeg'); res.send(buf);
  }catch(e){ res.status(500).json({error:e.message}) }
});
app.post('/api/gemini/spot', async(req,res)=>{
  try{
    if(!GEMINI) return res.status(500).json({error:'GEMINI_API_KEY nao configurada'});
    const b=req.body;
    const prompt='Crie um SPOT de radio de '+b.duracao+'s, estilo '+b.estilo+', em portugues brasileiro, para '+b.estabelecimento+': produto '+b.produto+', promocao '+b.promocao+', preco '+b.preco+', endereco '+b.endereco+', telefone '+b.telefone+'. Estrutura: chamada impactante, apresentacao, argumentos, preco, CTA final. Sem markdown.';
    const r=await fetch('https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key='+GEMINI,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({contents:[{parts:[{text:prompt}]}]})});
    const j=await r.json(); const text=j.candidates?.[0]?.content?.parts?.[0]?.text || 'Erro Gemini';
    res.json({text});
  }catch(e){ res.status(500).json({error:e.message}) }
});
app.get('/health',(req,res)=>res.json({ok:true, eleven:!!ELEVEN, gemini:!!GEMINI}));
const PORT=process.env.PORT||3000; app.listen(PORT,()=>console.log('VOZ DO BRASIL backend na porta '+PORT));
