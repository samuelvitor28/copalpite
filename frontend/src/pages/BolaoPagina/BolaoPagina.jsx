import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';
import './BolaoPagina.css';

const USUARIO_ID = 1; // trocar quando tiver JWT

function codigoParaEmoji(codigo) {
  if (!codigo) return '🏳️';
  const letras = codigo.toUpperCase().slice(0, 2);
  return letras
    .split('')
    .map((c) => String.fromCodePoint(0x1f1e6 + c.charCodeAt(0) - 65))
    .join('');
}

function formatarData(dataHora) {
  const d = new Date(dataHora);
  return {
    data: d.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' }),
    hora: d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' }) + 'h',
  };
}

function jogoAberto(dataHora) {
  return new Date(dataHora) > new Date();
}

// Agrupa jogos por rodada, depois por grupo
function agruparJogos(jogos) {
  const rodadas = {};
  jogos.forEach((jogo) => {
    const rodadaNome = jogo.rodada?.nome ?? 'Sem rodada';
    const rodadaId = jogo.rodada?.id ?? 0;
    const grupo = jogo.selecaoCasa?.grupo ?? 'Sem grupo';

    if (!rodadas[rodadaId]) {
      rodadas[rodadaId] = { nome: rodadaNome, id: rodadaId, grupos: {} };
    }
    if (!rodadas[rodadaId].grupos[grupo]) {
      rodadas[rodadaId].grupos[grupo] = [];
    }
    rodadas[rodadaId].grupos[grupo].push(jogo);
  });

  // ordenar rodadas por id, grupos alfabeticamente
  return Object.values(rodadas)
    .sort((a, b) => a.id - b.id)
    .map((r) => ({
      ...r,
      grupos: Object.entries(r.grupos)
        .sort(([a], [b]) => a.localeCompare(b))
        .map(([nome, jogos]) => ({ nome, jogos })),
    }));
}

export default function BolaoPagina() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [bolao, setBolao] = useState(null);
  const [rodadas, setRodadas] = useState([]);
  const [rodadaAberta, setRodadaAberta] = useState(null);
  const [palpites, setPalpites] = useState({});
  const [palpitesSalvos, setPalpitesSalvos] = useState({});
  const [ranking, setRanking] = useState([]);
  const [salvando, setSalvando] = useState({});
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function carregar() {
      try {
        const [bolaoRes, jogosRes, palpitesRes, rankingRes] = await Promise.all([
          api.get(`/bolaos/${id}`),
          api.get('/jogos'),
          api.get(`/palpites/bolao/${id}/usuario/${USUARIO_ID}`),
          api.get(`/bolaos/${id}/ranking`),
        ]);

        setBolao(bolaoRes.data);

        const jogosValidos = jogosRes.data
          .filter((j) => j.selecaoCasa?.nome && j.selecaoVisitante?.nome)
          .sort((a, b) => new Date(a.dataHora) - new Date(b.dataHora));

        const agrupados = agruparJogos(jogosValidos);
        setRodadas(agrupados);

        // abrir primeira rodada por padrão
        if (agrupados.length > 0) setRodadaAberta(agrupados[0].id);

        const salvos = {};
        const inputs = {};
        palpitesRes.data.forEach((p) => {
          const jogo = jogosRes.data.find((j) =>
            p.jogoDescricao?.includes(j.selecaoCasa?.nome) &&
            p.jogoDescricao?.includes(j.selecaoVisitante?.nome)
          );
          if (jogo) {
            salvos[jogo.id] = p;
            inputs[jogo.id] = { golsCasa: p.golsCasa, golsVisitante: p.golsVisitante };
          }
        });
        setPalpitesSalvos(salvos);
        setPalpites(inputs);
        setRanking(rankingRes.data);
      } catch {
        setErro('Erro ao carregar o bolão.');
      } finally {
        setLoading(false);
      }
    }
    carregar();
  }, [id]);

  function handleInput(jogoId, campo, valor) {
    const num = valor === '' ? '' : Math.max(0, parseInt(valor) || 0);
    setPalpites((prev) => ({
      ...prev,
      [jogoId]: {
        golsCasa: '',
        golsVisitante: '',
        ...prev[jogoId],
        [campo]: num,
      },
    }));
  }

  async function salvarPalpite(jogo) {
    const p = palpites[jogo.id];
    if (p?.golsCasa === '' || p?.golsCasa === undefined || p?.golsVisitante === '' || p?.golsVisitante === undefined) return;
    setSalvando((prev) => ({ ...prev, [jogo.id]: true }));
    try {
      const res = await api.post('/palpites', {
        usuarioId: USUARIO_ID,
        jogoId: jogo.id,
        bolaoId: Number(id),
        golsCasa: p.golsCasa,
        golsVisitante: p.golsVisitante,
      });
      setPalpitesSalvos((prev) => ({ ...prev, [jogo.id]: res.data }));
    } catch {
      // erro individual silenciado por enquanto
    } finally {
      setSalvando((prev) => ({ ...prev, [jogo.id]: false }));
    }
  }

  if (loading) return (
    <div className="page-root"><Navbar /><main className="page-main"><p className="bp-estado">Carregando…</p></main></div>
  );

  if (erro) return (
    <div className="page-root"><Navbar /><main className="page-main"><p className="bp-estado bp-estado--erro">{erro}</p></main></div>
  );

  return (
    <div className="page-root">
      <Navbar />
      <main className="page-main">

        {/* HEADER */}
        <div className="bp-header">
          <div>
            <button className="bp-voltar" onClick={() => navigate('/boloes/meus')}>← Meus bolões</button>
            <h1 className="bp-titulo">{bolao?.nome}</h1>
            <p className="bp-convite">Código de convite: <span className="bp-convite-codigo">#{bolao?.codigoConvite}</span></p>
          </div>
          <div className="bp-meta">
            <span className="bp-meta-item">{bolao?.totalParticipantes} participantes</span>
            <span className="bp-meta-sep">·</span>
            <span className="bp-meta-item">Admin: {bolao?.donoUsername}</span>
          </div>
        </div>

        <div className="bp-grid">

          {/* JOGOS AGRUPADOS */}
          <section className="bp-section">

            {/* TABS DE RODADA */}
            <div className="bp-rodada-tabs">
              {rodadas.map((r) => (
                <button
                  key={r.id}
                  className={`bp-rodada-tab ${rodadaAberta === r.id ? 'bp-rodada-tab--ativo' : ''}`}
                  onClick={() => setRodadaAberta(r.id)}
                >
                  {r.nome}
                </button>
              ))}
            </div>

            {/* GRUPOS DA RODADA ATIVA */}
            {rodadas
              .filter((r) => r.id === rodadaAberta)
              .map((rodada) => (
                <div key={rodada.id}>
                  {rodada.grupos.map((grupo) => (
                    <div key={grupo.nome} className="bp-grupo">
                      <h3 className="bp-grupo-titulo">Grupo {grupo.nome}</h3>
                      <ul className="bp-jogos-lista">
                        {grupo.jogos.map((jogo) => {
                          const { data, hora } = formatarData(jogo.dataHora);
                          const aberto = jogoAberto(jogo.dataHora);
                          const emojicasa = codigoParaEmoji(jogo.selecaoCasa?.codigoSelecao);
                          const emojiVisitante = codigoParaEmoji(jogo.selecaoVisitante?.codigoSelecao);
                          const codCasa = jogo.selecaoCasa?.codigoSelecao ?? '';
                          const codVisitante = jogo.selecaoVisitante?.codigoSelecao ?? '';
                          const p = palpites[jogo.id] ?? { golsCasa: '', golsVisitante: '' };
                          const salvo = palpitesSalvos[jogo.id];

                          return (
                            <li key={jogo.id} className={`bp-jogo-card ${salvo ? 'bp-jogo-card--salvo' : ''}`}>
                              <div className="bp-jogo-data">
                                <span>{data}</span>
                                <span className="bp-jogo-hora">{hora}</span>
                              </div>

                              <div className="bp-jogo-times">
                                <span className="bp-jogo-time">
                                  {emojicasa} <span className="bp-jogo-codigo">{codCasa}</span>
                                </span>

                                {aberto ? (
                                  <div className="bp-placar-input">
                                    <input
                                      className="bp-input-gol"
                                      type="number"
                                      min="0"
                                      value={p.golsCasa}
                                      onChange={(e) => handleInput(jogo.id, 'golsCasa', e.target.value)}
                                    />
                                    <span className="bp-input-sep">×</span>
                                    <input
                                      className="bp-input-gol"
                                      type="number"
                                      min="0"
                                      value={p.golsVisitante}
                                      onChange={(e) => handleInput(jogo.id, 'golsVisitante', e.target.value)}
                                    />
                                  </div>
                                ) : (
                                  <div className="bp-placar-fechado">
                                    {salvo ? `${salvo.golsCasa} × ${salvo.golsVisitante}` : '— × —'}
                                  </div>
                                )}

                                <span className="bp-jogo-time bp-jogo-time--direita">
                                  <span className="bp-jogo-codigo">{codVisitante}</span> {emojiVisitante}
                                </span>
                              </div>

                              {aberto ? (
                                <button
                                  className={`bp-btn-salvar ${salvo ? 'bp-btn-salvar--atualizar' : ''}`}
                                  onClick={() => salvarPalpite(jogo)}
                                  disabled={salvando[jogo.id]}
                                >
                                  {salvando[jogo.id] ? 'Salvando…' : salvo ? 'Atualizar' : 'Salvar'}
                                </button>
                              ) : (
                                <span className="bp-jogo-fechado-label">Encerrado</span>
                              )}
                            </li>
                          );
                        })}
                      </ul>
                    </div>
                  ))}
                </div>
              ))}
          </section>

          {/* RANKING */}
          <section className="bp-section bp-section--ranking">
            <h2 className="bp-section-titulo">Ranking do bolão</h2>
            {ranking.length === 0 && <p className="bp-estado">Nenhum palpite ainda.</p>}
            <ul className="bp-ranking-lista">
              {ranking.map((item, idx) => (
                <li key={item.usuarioId ?? idx} className={`bp-ranking-item ${item.usuarioId === USUARIO_ID ? 'bp-ranking-item--eu' : ''}`}>
                  <span className={`bp-ranking-pos ${idx === 0 ? 'ouro' : idx === 1 ? 'prata' : idx === 2 ? 'bronze' : ''}`}>
                    {idx === 0 ? '🥇' : idx === 1 ? '🥈' : idx === 2 ? '🥉' : `#${idx + 1}`}
                  </span>
                  <span className="bp-ranking-nome">{item.usernameUsuario}</span>
                  <span className="bp-ranking-pontos">{item.pontos ?? 0} pts</span>
                </li>
              ))}
            </ul>
          </section>

        </div>
      </main>
    </div>
  );
}