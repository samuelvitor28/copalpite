import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './Dashboard.css';

const usuario = {
  username: 'samuel',
  pontuacao: 347,
};

const ranking = [
  { pos: 1, nome: 'Gabriel', pontos: 412 },
  { pos: 2, nome: 'Samuel', pontos: 347 },
  { pos: 3, nome: 'Lucas', pontos: 298 },
  { pos: 4, nome: 'Mariana', pontos: 265 },
  { pos: 5, nome: 'Pedro', pontos: 201 },
];

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
  const navigate = useNavigate();

  useEffect(() => {
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
  }, []);

  return (
    <div className="dash-root">
      {/* NAVBAR */}
      <header className="dash-navbar">
        <span className="dash-nav-logo">Copalpite</span>

        <nav className="dash-nav-links">
          <button className="dash-nav-btn dash-nav-btn--destaque" onClick={() => navigate('/boloes/criar')}>+ Criar bolão</button>
          <button className="dash-nav-btn" onClick={() => navigate('/boloes/entrar')}>Entrar em bolão</button>
          <button className="dash-nav-btn" onClick={() => navigate('/boloes/meus')}>Meus bolões</button>
          <button className="dash-nav-btn">Adicionar amigo</button>
        </nav>

        <div className="dash-nav-usuario" onClick={() => setMenuAberto(!menuAberto)}>
          <div className="dash-avatar">{usuario.username[0].toUpperCase()}</div>
          <span className="dash-nav-username">@{usuario.username}</span>
          <span className="dash-nav-caret">▾</span>

          {menuAberto && (
            <div className="dash-dropdown">
              <a href="/perfil" className="dash-dropdown-item">Meu perfil</a>
              <a href="/configuracoes" className="dash-dropdown-item">Configurações</a>
              <div className="dash-dropdown-divider" />
              <a href="/" className="dash-dropdown-item dash-dropdown-item--sair">Sair</a>
            </div>
          )}
        </div>
      </header>

      {/* CONTEÚDO */}
      <main className="dash-main">

        {/* PONTUAÇÃO DO USUÁRIO */}
        <section className="dash-card dash-pontuacao">
          <p className="dash-label">Sua pontuação</p>
          <div className="dash-pontos-valor">{usuario.pontuacao}</div>
          <p className="dash-pontos-sub">pontos acumulados</p>
        </section>

        {/* RANKING */}
        <section className="dash-card dash-ranking">
          <h2 className="dash-card-titulo">Ranking de amigos</h2>
          <ul className="dash-ranking-lista">
            {ranking.map((item) => (
              <li
                key={item.pos}
                className={`dash-ranking-item ${item.nome.toLowerCase() === usuario.username ? 'dash-ranking-item--eu' : ''}`}
              >
                <span className={`dash-ranking-pos ${item.pos === 1 ? 'dash-ranking-pos--ouro' : item.pos === 2 ? 'dash-ranking-pos--prata' : item.pos === 3 ? 'dash-ranking-pos--bronze' : ''}`}>
                  {item.pos === 1 ? '🥇' : item.pos === 2 ? '🥈' : item.pos === 3 ? '🥉' : `#${item.pos}`}
                </span>
                <span className="dash-ranking-nome">{item.nome}</span>
                <span className="dash-ranking-pontos">{item.pontos} pts</span>
              </li>
            ))}
          </ul>
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
                const emojicasa = codigoParaEmoji(jogo.selecaoCasa?.codigoSelecao);
                const emojiVisitante = codigoParaEmoji(jogo.selecaoVisitante?.codigoSelecao);
                return (
                  <li key={jogo.id} className="dash-jogo-item">
                    <div className="dash-jogo-times">
                      <span className="dash-jogo-time">{emojicasa} {jogo.selecaoCasa?.nome}</span>
                      <span className="dash-jogo-vs">vs</span>
                      <span className="dash-jogo-time">{emojiVisitante} {jogo.selecaoVisitante?.nome}</span>
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