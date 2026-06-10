import { useState } from 'react';
import { useNavigate } from 'react-router-dom';


const usuario = {
  username: 'samuel',
};

export default function Navbar() {
  const [menuAberto, setMenuAberto] = useState(false);
  const navigate = useNavigate();

  return (
    <header className="dash-navbar">
      <span className="dash-nav-logo" onClick={() => navigate('/dashboard')} style={{ cursor: 'pointer' }}>
        Copalpite
      </span>

      <nav className="dash-nav-links">
        <button className="dash-nav-btn dash-nav-btn--destaque" onClick={() => navigate('/boloes/criar')}>
          + Criar bolão
        </button>
        <button className="dash-nav-btn" onClick={() => navigate('/boloes/entrar')}>
          Entrar em bolão
        </button>
        <button className="dash-nav-btn" onClick={() => navigate('/boloes/meus')}>
          Meus bolões
        </button>
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
  );
}