import { useEffect, useState } from 'react';
import './Toast.css';
 
export default function Toast({ mensagem, tipo = 'sucesso', onClose }) {
  const [saindo, setSaindo] = useState(false);
 
  useEffect(() => {
    const timer = setTimeout(() => setSaindo(true), 3000);
    const remove = setTimeout(() => onClose(), 3500);
    return () => { clearTimeout(timer); clearTimeout(remove); };
  }, [onClose]);
 
  return (
    <div className={`toast toast--${tipo} ${saindo ? 'toast--saindo' : ''}`}>
      <span className="toast-icone">{tipo === 'sucesso' ? '✓' : '✕'}</span>
      <span className="toast-mensagem">{mensagem}</span>
    </div>
  );
}
 