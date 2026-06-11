  import { useState } from 'react';
  import { useNavigate } from 'react-router-dom';
  import api from '../../services/api';
  import Navbar from '../../components/Navbar/Navbar';
  import '../../styles/Shared.css'

  export default function EntrarBolao() {
    const navigate = useNavigate();
    const [codigo, setCodigo] = useState('');
    const [loading, setLoading] = useState(false);
    const [erro, setErro] = useState('');

    async function handleEntrar() {
      const codigoLimpo = codigo.trim().toUpperCase();
      if (!codigoLimpo) {
        setErro('Insira o código do bolão.');
        return;
      }
      setLoading(true);
      setErro('');
      try {
        const usuarioId = Number(localStorage.getItem('usuarioId'));
        const res = await api.post('/boloes/entrar', {
          codigoConvite: codigoLimpo,
          usuarioId,
        });
        navigate(`/boloes/${res.data.id}`);
      } catch (err) {
        const msg = err?.response?.data?.message;
        setErro(msg || 'Código inválido ou bolão não encontrado.');
      } finally {
        setLoading(false);
      }
    }

    function handleKeyDown(e) {
      if (e.key === 'Enter') handleEntrar();
    }

    return (
      <div className="page-root">
        <Navbar />
        <main className="page-main page-main--centro">
          <div className="form-card">
            <div className="form-card-header">
              <span className="form-card-icone">🔑</span>
              <h1 className="form-card-titulo">Entrar em bolão</h1>
              <p className="form-card-sub">Peça o código de convite para quem criou o bolão e cole abaixo.</p>
            </div>

            <div className="form-campos">
              <div className="form-grupo">
                <label className="form-label" htmlFor="codigo">Código de convite</label>
                <input
                  id="codigo"
                  className="form-input form-input--codigo"
                  type="text"
                  placeholder="Ex: AB12CD"
                  value={codigo}
                  onChange={(e) => setCodigo(e.target.value.toUpperCase())}
                  onKeyDown={handleKeyDown}
                  maxLength={12}
                  autoComplete="off"
                  spellCheck={false}
                />
              </div>

              {erro && <p className="form-erro">{erro}</p>}

              <div className="form-acoes">
                <button className="form-btn form-btn--secundario" onClick={() => navigate('/dashboard')}>
                  Cancelar
                </button>
                <button
                  className="form-btn form-btn--primario"
                  onClick={handleEntrar}
                  disabled={loading}
                >
                  {loading ? 'Entrando…' : 'Entrar'}
                </button>
              </div>
            </div>
          </div>
        </main>
      </div>
    );
  }