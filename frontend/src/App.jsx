import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/Login/Login';
import Cadastro from './pages/Cadastro/Cadastro';
import Dashboard from './pages/Dashboard/Dashboard';
import CriarBolao from './pages/CriarBolao/CriarBolao';
import EntrarBolao from './pages/EntrarBolao/EntrarBolao';
import MeusBoloes from './pages/MeusBoloes/MeusBoloes';
import BolaoPagina from './pages/BolaoPagina/BolaoPagina';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/boloes/criar" element={<CriarBolao />} />
        <Route path="/boloes/entrar" element={<EntrarBolao />} />
        <Route path="/boloes/meus" element={<MeusBoloes />} />
        <Route path="/boloes/:id" element={<BolaoPagina />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;