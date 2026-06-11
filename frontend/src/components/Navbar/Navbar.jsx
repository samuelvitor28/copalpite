import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Navbar() {
  const [menuAberto, setMenuAberto] = useState(false);
  const navigate = useNavigate();

  const username = localStorage.getItem('username') ?? 'usuário';

  function sair() {
    localStorage.clear();
    navigate('/');
  }

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
      </nav>

      <div className="dash-nav-usuario" onClick={() => setMenuAberto(!menuAberto)}>
        <div className="dash-avatar">{username[0].toUpperCase()}</div>
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
  );
}