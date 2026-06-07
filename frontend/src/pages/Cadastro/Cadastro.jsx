import { useState, useEffect } from 'react';
import api from '../../services/api';
import './Cadastro.css';

function codigoParaEmoji(codigo) {
  if (!codigo) return '🏳️';
  return codigo
    .toUpperCase()
    .slice(0, 2)
    .split('')
    .map((c) => String.fromCodePoint(0x1f1e6 + c.charCodeAt(0) - 65))
    .join('');
}

export default function Cadastro() {
  const [form, setForm] = useState({
    username: '',
    nome: '',
    ultimoNome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
    selecaoTorcidaId: '',
  });
  const [selecoes, setSelecoes] = useState([]);
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState(false);
  const [carregando, setCarregando] = useState(false);

  useEffect(() => {
    api.get('/selecoes')
      .then((res) => setSelecoes(res.data))
      .catch(() => setSelecoes([]));
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErro('');
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setErro('');

    if (form.senha !== form.confirmarSenha) {
      setErro('As senhas não coincidem.');
      return;
    }

    setCarregando(true);

    try {
      const { confirmarSenha, ...payload } = form;
      const dados = {
        ...payload,
        selecaoTorcidaId: payload.selecaoTorcidaId ? Number(payload.selecaoTorcidaId) : null,
      };
      await api.post('/usuarios', dados);
      setSucesso(true);
    } catch (err) {
      const mensagem = err.response?.data?.message || 'Erro ao cadastrar. Tente novamente.';
      setErro(mensagem);
    } finally {
      setCarregando(false);
    }
  }

  if (sucesso) {
    return (
      <div className="cadastro-container">
        <div className="cadastro-card">
          <div className="cadastro-sucesso">
            <span className="cadastro-sucesso-icone">✓</span>
            <h2>Conta criada!</h2>
            <p>Bem-vindo ao Copalpite. Agora é só entrar e fazer seus palpites.</p>
            <a href="/" className="cadastro-btn-link">Ir para o login</a>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="cadastro-container">
      <div className="cadastro-card">
        <div className="cadastro-header">
          <h1 className="cadastro-logo">Copalpite</h1>
          <p className="cadastro-subtitulo">Crie sua conta e entre na disputa.</p>
        </div>

        <form className="cadastro-form" onSubmit={handleSubmit}>
          <div className="cadastro-linha">
            <div className="cadastro-campo">
              <label htmlFor="nome">Nome</label>
              <input
                id="nome"
                name="nome"
                type="text"
                value={form.nome}
                onChange={handleChange}
                placeholder="Primeiro Nome"
                required
              />
            </div>
            <div className="cadastro-campo">
              <label htmlFor="ultimoNome">Último nome</label>
              <input
                id="ultimoNome"
                name="ultimoNome"
                type="text"
                value={form.ultimoNome}
                onChange={handleChange}
                placeholder="Ultimo Nome"
                required
              />
            </div>
          </div>

          <div className="cadastro-campo">
            <label htmlFor="username">Usuário</label>
            <input
              id="username"
              name="username"
              type="text"
              value={form.username}
              onChange={handleChange}
              placeholder="Digite um nome de usuário..."
              minLength={6}
              maxLength={50}
              required
            />
          </div>

          <div className="cadastro-campo">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              placeholder="Digite seu email..."
              required
            />
          </div>

          <div className="cadastro-linha">
            <div className="cadastro-campo">
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
            <div className="cadastro-campo">
              <label htmlFor="confirmarSenha">Confirmar senha</label>
              <input
                id="confirmarSenha"
                name="confirmarSenha"
                type="password"
                value={form.confirmarSenha}
                onChange={handleChange}
                placeholder="••••••••"
                required
              />
            </div>
          </div>

          <div className="cadastro-campo">
            <label htmlFor="selecaoTorcidaId">Qual seleção você está torcendo?</label>
            <select
              id="selecaoTorcidaId"
              name="selecaoTorcidaId"
              value={form.selecaoTorcidaId}
              onChange={handleChange}
              className="cadastro-select"
            >
              <option value="">Escolha sua seleção</option>
              {selecoes.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.nome}
                </option>
              ))}
            </select>
          </div>

          {erro && <p className="cadastro-erro">{erro}</p>}

          <button className="cadastro-btn" type="submit" disabled={carregando}>
            {carregando ? 'Cadastrando...' : 'Criar conta'}
          </button>
        </form>

        <p className="cadastro-login">
          Já tem conta? <a href="/">Entrar</a>
        </p>
      </div>
    </div>
  );
}