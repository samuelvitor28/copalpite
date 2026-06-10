import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';
import './MeusBoloes.css';

export default function MeusBoloes() {
  const navigate = useNavigate();
  const [boloes, setBoloes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    const usuarioId = localStorage.getItem('usuarioId');
    api.get(`/boloes/meus?usuarioId=${usuarioId}`)
      .then((res) => setBoloes(res.data))
      .catch(() => setErro('Não foi possível carregar seus bolões.'))
      .finally(() => setLoading(false));
  }, []);
  return (
    <div className="page-root">
      <Navbar />
      <main className="page-main">
        <div className="mb-header">
          <div>
            <h1 className="mb-titulo">Meus bolões</h1>
            <p className="mb-sub">Todos os bolões que você participa ou administra.</p>
          </div>
          <div className="mb-header-acoes">
            <button className="form-btn form-btn--secundario" onClick={() => navigate('/boloes/entrar')}>
              Entrar com código
            </button>
            <button className="form-btn form-btn--primario" onClick={() => navigate('/boloes/criar')}>
              + Criar bolão
            </button>
          </div>
        </div>

        {loading && <p className="mb-estado">Carregando…</p>}
        {erro && <p className="mb-estado mb-estado--erro">{erro}</p>}

        {!loading && !erro && boloes.length === 0 && (
          <div className="mb-vazio">
            <span className="mb-vazio-icone">🏆</span>
            <p className="mb-vazio-titulo">Você ainda não participa de nenhum bolão.</p>
            <p className="mb-vazio-sub">Crie um novo ou entre com um código de convite.</p>
          </div>
        )}

        {!loading && boloes.length > 0 && (
          <ul className="mb-lista">
            {boloes.map((b) => (
              <li
                key={b.id}
                className="mb-card"
                onClick={() => navigate(`/boloes/${b.id}`)}
              >
                <div className="mb-card-top">
                  <span className="mb-card-nome">{b.nome}</span>
                  {b.admin && <span className="mb-badge mb-badge--admin">Admin</span>}
                </div>
                {b.descricao && <p className="mb-card-desc">{b.descricao}</p>}
                <div className="mb-card-rodape">
                  <span className="mb-card-info">{b.totalParticipantes ?? '—'} participantes</span>
                  <span className="mb-card-codigo">#{b.codigoConvite}</span>
                </div>
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  );
}