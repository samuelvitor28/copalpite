import { useState, useEffect } from 'react';
import api from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';
import './Perfil.css';

const USUARIO_ID = Number(localStorage.getItem('usuarioId'));
const USERNAME = localStorage.getItem('username');
const POR_PAGINA = 10;

export default function Perfil() {
  const [palpites, setPalpites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');
  const [pagina, setPagina] = useState(1);

  useEffect(() => {
    api.get(`/palpites/usuario/${USUARIO_ID}`)
      .then((res) => setPalpites(res.data))
      .catch(() => setErro('Não foi possível carregar os palpites.'))
      .finally(() => setLoading(false));
  }, []);

  const comResultado = palpites.filter((p) => p.pontos !== null && p.pontos !== undefined);
  const total = comResultado.length;
  const exatos = comResultado.filter((p) => p.pontos === 3).length;
  const vencedor = comResultado.filter((p) => p.pontos === 1).length;
  const erros = comResultado.filter((p) => p.pontos === 0).length;
  const totalPontos = comResultado.reduce((acc, p) => acc + (p.pontos ?? 0), 0);
  const taxaAcerto = total > 0 ? Math.round(((exatos + vencedor) / total) * 100) : null;

  const totalPaginas = Math.ceil(palpites.length / POR_PAGINA);
  const palpitesPagina = palpites.slice((pagina - 1) * POR_PAGINA, pagina * POR_PAGINA);

  return (
    <div className="page-root">
      <Navbar />
      <main className="page-main">
        <div className="perfil-root">

          {/* Header */}
          <div className="perfil-header">
            <div className="perfil-avatar">
              {USERNAME?.[0]?.toUpperCase() ?? '?'}
            </div>
            <div className="perfil-header-info">
              <h1>{USERNAME}</h1>
              <p>{palpites.length} palpites feitos</p>
            </div>
          </div>

          {/* Stats */}
          {total > 0 && (
            <div className="perfil-stats">
              <StatCard label="Pontos totais" value={totalPontos} cor="#00c864" />
              <StatCard label="Placar exato" value={exatos} sub="3 pts cada" cor="#00c864" />
              <StatCard label="Vencedor certo" value={vencedor} sub="1 pt cada" cor="#f5c518" />
              <StatCard label="Erros" value={erros} cor="#ff6b6b" />
              {taxaAcerto !== null && (
                <StatCard label="Taxa de acerto" value={`${taxaAcerto}%`} cor="#00c864" />
              )}
            </div>
          )}

          {/* Lista */}
          <h2 className="perfil-secao-titulo">Histórico de palpites</h2>

          {loading && <p className="perfil-estado">Carregando…</p>}
          {erro && <p className="perfil-estado perfil-estado--erro">{erro}</p>}

          {!loading && !erro && palpites.length === 0 && (
            <div className="perfil-vazio">
              <span className="perfil-vazio-icone">🎯</span>
              <p>Nenhum palpite feito ainda.</p>
            </div>
          )}

          {!loading && palpitesPagina.length > 0 && (
            <>
              <ul className="perfil-lista">
                {palpitesPagina.map((p) => {
                  const resultado =
                    p.pontos === 3 ? 'exato' :
                    p.pontos === 1 ? 'vencedor' :
                    p.pontos === 0 ? 'erro' : 'pendente';

                  return (
                    <li key={p.id} className={`perfil-palpite-card perfil-palpite-card--${resultado}`}>
                      <span className="perfil-palpite-jogo">{p.jogoDescricao}</span>
                      <span className="perfil-palpite-placar">{p.golsCasa} × {p.golsVisitante}</span>
                      <span className={`perfil-badge perfil-badge--${resultado}`}>
                        {resultado === 'exato'    && '🎯 Exato · 3pts'}
                        {resultado === 'vencedor' && '✅ Vencedor · 1pt'}
                        {resultado === 'erro'     && '❌ Errou · 0pts'}
                        {resultado === 'pendente' && '⏳ Pendente'}
                      </span>
                    </li>
                  );
                })}
              </ul>

              {totalPaginas > 1 && (
                <div className="perfil-paginacao">
                  <button
                    className="perfil-pag-btn"
                    onClick={() => setPagina((p) => p - 1)}
                    disabled={pagina === 1}
                  >
                    ← Anterior
                  </button>

                  <div className="perfil-pag-paginas">
                    {Array.from({ length: totalPaginas }, (_, i) => i + 1).map((n) => (
                      <button
                        key={n}
                        className={`perfil-pag-num ${pagina === n ? 'perfil-pag-num--ativo' : ''}`}
                        onClick={() => setPagina(n)}
                      >
                        {n}
                      </button>
                    ))}
                  </div>

                  <button
                    className="perfil-pag-btn"
                    onClick={() => setPagina((p) => p + 1)}
                    disabled={pagina === totalPaginas}
                  >
                    Próximo →
                  </button>
                </div>
              )}
            </>
          )}

        </div>
      </main>
    </div>
  );
}

function StatCard({ label, value, sub, cor }) {
  return (
    <div className="perfil-stat-card">
      <div className="perfil-stat-valor" style={{ color: cor }}>{value}</div>
      <div className="perfil-stat-label">{label}</div>
      {sub && <div className="perfil-stat-sub">{sub}</div>}
    </div>
  );
}