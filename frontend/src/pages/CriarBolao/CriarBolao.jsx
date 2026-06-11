import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';

export default function CriarBolao() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ nome: '', descricao: '' });
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');

  function handleChange(e) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (!form.nome.trim()) {
      setErro('O nome do bolão é obrigatório.');
      return;
    }
    setLoading(true);
    setErro('');
    try {
      const donoId = Number(localStorage.getItem('usuarioId')); // ← era 'usuario'
      const res = await api.post('/boloes', { ...form, donoId });
      navigate(`/boloes/${res.data.id}`);
    } catch {
      setErro('Erro ao criar bolão. Tente novamente.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page-root">
      <Navbar />
      <main className="page-main page-main--centro">
        <div className="form-card">
          <div className="form-card-header">
            <span className="form-card-icone">🏆</span>
            <h1 className="form-card-titulo">Criar bolão</h1>
            <p className="form-card-sub">Configure seu bolão e compartilhe o código com os participantes.</p>
          </div>

          <div className="form-campos">
            <div className="form-grupo">
              <label className="form-label" htmlFor="nome">Nome do bolão</label>
              <input
                id="nome"
                name="nome"
                className="form-input"
                type="text"
                placeholder="Ex: Bolão do escritório"
                value={form.nome}
                onChange={handleChange}
                maxLength={60}
              />
            </div>

            <div className="form-grupo">
              <label className="form-label" htmlFor="descricao">Descrição <span className="form-label-opcional">(opcional)</span></label>
              <textarea
                id="descricao"
                name="descricao"
                className="form-input form-textarea"
                placeholder="Uma descrição rápida do grupo..."
                value={form.descricao}
                onChange={handleChange}
                maxLength={200}
                rows={3}
              />
            </div>

            {erro && <p className="form-erro">{erro}</p>}

            <div className="form-acoes">
              <button className="form-btn form-btn--secundario" onClick={() => navigate('/dashboard')}>
                Cancelar
              </button>
              <button
                className="form-btn form-btn--primario"
                onClick={handleSubmit}
                disabled={loading}
              >
                {loading ? 'Criando…' : 'Criar bolão'}
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}