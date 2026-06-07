import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import Toast from '../../components/Toast/Toast';
import './Login.css';

export default function Login() {
  const [form, setForm] = useState({ username: '', senha: '' });
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);
  const [toast, setToast] = useState(null);
  const navigate = useNavigate();

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErro('');
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setCarregando(true);
    setErro('');

    try {
      const resposta = await api.get(`/usuarios/username/${form.username}`);
      setToast({ mensagem: `Bem-vindo, ${resposta.data.username}!`, tipo: 'sucesso' });
      setTimeout(() => navigate('/dashboard'), 1500);
    } catch (err) {
      setErro('Usuário não encontrado ou erro na conexão.');
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="login-container">
      {toast && (
        <Toast
          mensagem={toast.mensagem}
          tipo={toast.tipo}
          onClose={() => setToast(null)}
        />
      )}

      <div className="login-card">
        <div className="login-header">
          <h1 className="login-logo">Copalpite</h1>
          <p className="login-subtitulo">Faça seu palpite. Prove que sabe mais que seus amigos.</p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="login-campo">
            <label htmlFor="username">Usuário</label>
            <input
              id="username"
              name="username"
              type="text"
              value={form.username}
              onChange={handleChange}
              placeholder="seu_usuario"
              required
            />
          </div>

          <div className="login-campo">
            <label htmlFor="senha">Senha</label>
            <input
              id="senha"
              name="senha"
              type="password"
              value={form.senha}
              onChange={handleChange}
              placeholder="••••••••"
              required
            />
          </div>

          {erro && <p className="login-erro">{erro}</p>}

          <button className="login-btn" type="submit" disabled={carregando}>
            {carregando ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <p className="login-cadastro">
          Não tem conta? <a href="/cadastro">Cadastre-se</a>
        </p>
      </div>
    </div>
  );
}