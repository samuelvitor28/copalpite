import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Dashboard.css';

function codigoParaEmoji(codigo) {
  if (!codigo) return '🏳️';
  return codigo
    .toUpperCase()
    .slice(0, 2)
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

export default function Dashboard() {
  const [menuAberto, setMenuAberto] = useState(false);
  const [proximosJogos, setProximosJogos] = useState([]);
  const [ranking, setRanking] = useState([]);
  const [pontuacao, setPontuacao] = useState(0);
  const navigate = useNavigate();

  const usuarioId = localStorage.getItem('usuarioId');
  const username = localStorage.getItem('username');

  useEffect(() => {
    // Próximos jogos
    api.get('/jogos')
      .then((res) => {
        const agora = new Date();
        const futuros = res.data
          .filter((j) => new Date(j.dataHora) > agora)
          .sort((a, b) => new Date(a.dataHora) - new Date(b.dataHora))
          .slice(0, 6);
        setProximosJogos(futuros);
      })
      .catch(() => setProximosJogos([]));

    // Ranking geral
    api.get('/usuarios/ranking')
      .then((res) => {
        setRanking(res.data);
        const eu = res.data.find((r) => String(r.usuarioId) === String(usuarioId));
        if (eu) setPontuacao(eu.pontos ?? 0);
      })
      .catch(() => setRanking([]));
  }, []);

  function sair() {
    localStorage.clear();
    navigate('/');
  }

  return (
    <div className="dash-root">
      {/* NAVBAR */}
      <header className="dash-navbar">
        <span className="dash-nav-logo">Copalpite</span>

        <nav className="dash-nav-links">
          <button className="dash-nav-btn dash-nav-btn--destaque" onClick={() => navigate('/boloes/criar')}>+ Criar bolão</button>
          <button className="dash-nav-btn" onClick={() => navigate('/boloes/entrar')}>Entrar em bolão</button>
          <button className="dash-nav-btn" onClick={() => navigate('/boloes/meus')}>Meus bolões</button>
        </nav>

        <div className="dash-nav-usuario" onClick={() => setMenuAberto(!menuAberto)}>
          <div className="dash-avatar">{username?.[0]?.toUpperCase()}</div>
          <span className="dash-nav-username">@{username}</span>
          <span className="dash-nav-caret">▾</span>

          {menuAberto && (
            <div className="dash-dropdown">
              <a href="/perfil" className="dash-dropdown-item">Meu perfil</a>
              <div className="dash-dropdown-divider" />
              <button className="dash-dropdown-item dash-dropdown-item--sair" onClick={sair}>Sair</button>
            </div>
          )}
        </div>
      </header>

      {/* CONTEÚDO */}
      <main className="dash-main">

        {/* PONTUAÇÃO DO USUÁRIO */}
        <section className="dash-card dash-pontuacao">
          <p className="dash-label">Sua pontuação</p>
          <div className="dash-pontos-valor">{pontuacao}</div>
          <p className="dash-pontos-sub">pontos acumulados</p>
        </section>

        {/* RANKING */}
        <section className="dash-card dash-ranking">
          <h2 className="dash-card-titulo">Ranking geral</h2>
          {ranking.length === 0 ? (
            <p className="dash-jogos-vazio">Nenhum palpite pontuado ainda.</p>
          ) : (
            <ul className="dash-ranking-lista">
              {ranking.map((item, idx) => (
                <li
                  key={item.usuarioId}
                  className={`dash-ranking-item ${String(item.usuarioId) === String(usuarioId) ? 'dash-ranking-item--eu' : ''}`}
                >
                  <span className={`dash-ranking-pos ${idx === 0 ? 'dash-ranking-pos--ouro' : idx === 1 ? 'dash-ranking-pos--prata' : idx === 2 ? 'dash-ranking-pos--bronze' : ''}`}>
                    {idx === 0 ? '🥇' : idx === 1 ? '🥈' : idx === 2 ? '🥉' : `#${idx + 1}`}
                  </span>
                  <span className="dash-ranking-nome">{item.username}</span>
                  <span className="dash-ranking-pontos">{item.pontos ?? 0} pts</span>
                </li>
              ))}
            </ul>
          )}
        </section>

        {/* PRÓXIMOS JOGOS */}
        <section className="dash-card dash-jogos">
          <h2 className="dash-card-titulo">Próximos jogos</h2>
          {proximosJogos.length === 0 ? (
            <p className="dash-jogos-vazio">Nenhum jogo agendado.</p>
          ) : (
            <ul className="dash-jogos-lista">
              {proximosJogos.map((jogo) => {
                const { data, hora } = formatarData(jogo.dataHora);
                return (
                  <li key={jogo.id} className="dash-jogo-item">
                    <div className="dash-jogo-times">
                      <span className="dash-jogo-time">
                        <img src={jogo.selecaoCasa?.bandeira} alt={jogo.selecaoCasa?.codigoSelecao} className="dash-bandeira" />
                        {jogo.selecaoCasa?.codigoSelecao}
                      </span>
                      <span className="dash-jogo-vs">vs</span>
                      <span className="dash-jogo-time">
                        <img src={jogo.selecaoVisitante?.bandeira} alt={jogo.selecaoVisitante?.codigoSelecao} className="dash-bandeira" />
                        {jogo.selecaoVisitante?.codigoSelecao}
                      </span>
                    </div>
                    <div className="dash-jogo-info">
                      <span>{data}</span>
                      <span className="dash-jogo-hora">{hora}</span>
                    </div>
                  </li>
                );
              })}
            </ul>
          )}
        </section>

      </main>
    </div>
  );
}