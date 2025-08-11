import React, { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

// --- ÍCONES (SVG) ---
// Usamos SVGs embutidos para evitar dependências externas e garantir que tudo funcione em um único arquivo.
const MilkIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6">
    <path d="M8 2h8"></path><path d="M9 2v2.38c0 1.26.83 2.4 2 2.62.95.17 1.8.78 2.38 1.5.38.46.62 1.04.62 1.65V15c0 2.2-1.8 4-4 4h-1c-2.2 0-4-1.8-4-4V8.15c0-.6.24-1.19.62-1.65.58-.72 1.43-1.33 2.38-1.5C7.17 4.78 8 3.64 8 2.38V2"></path><path d="M5 10h14"></path><path d="M5 14h14"></path>
  </svg>
);

const UsersIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6">
    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M22 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
  </svg>
);

const ClipboardIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6">
    <rect width="8" height="4" x="8" y="2" rx="1" ry="1"></rect><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path>
  </svg>
);

const CalendarIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6">
    <rect width="18" height="18" x="3" y="4" rx="2" ry="2"></rect><line x1="16" x2="16" y1="2" y2="6"></line><line x1="8" x2="8" y1="2" y2="6"></line><line x1="3" x2="21" y1="10" y2="10"></line>
  </svg>
);

const ChartIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6">
    <path d="M3 3v18h18"></path><path d="M18 17V9"></path><path d="M13 17V5"></path><path d="M8 17v-3"></path>
  </svg>
);

const DashboardIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6"><rect width="7" height="9" x="3" y="3" rx="1"></rect><rect width="7" height="5" x="14" y="3" rx="1"></rect><rect width="7" height="9" x="14" y="12" rx="1"></rect><rect width="7" height="5" x="3" y="16" rx="1"></rect></svg>
);

const LogoutIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-6 w-6"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" x2="9" y1="12" y2="12"></line></svg>
);


// --- SERVIÇO DE API ---
// Centraliza todas as chamadas `fetch` para a nossa API backend.
const API_BASE_URL = 'http://localhost:8080'; // URL base da sua API Spring Boot

const apiService = {
  // Função para registro de usuário
  register: (nome, email, senha) => {
    return fetch(`${API_BASE_URL}/api/auth/registrar`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome, email, senha }),
    });
  },
  // Função para login de usuário
  login: (email, senha) => {
    return fetch(`${API_BASE_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, senha }),
    });
  },
  // Exemplo de função para buscar dados protegidos (precisa de token)
  getProducers: (token) => {
    return fetch(`${API_BASE_URL}/api/produtores`, { // Supondo que exista um endpoint /api/produtores
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  },
};

// --- COMPONENTES DE UI ---

// Componente para notificações (toast)
const Notification = ({ message, type, onDismiss }) => {
  if (!message) return null;

  const baseStyle = "fixed top-5 right-5 p-4 rounded-lg shadow-lg text-white transition-opacity duration-300";
  const typeStyle = type === 'success' ? 'bg-green-500' : 'bg-red-500';

  useEffect(() => {
    const timer = setTimeout(() => {
      onDismiss();
    }, 3000);
    return () => clearTimeout(timer);
  }, [message, onDismiss]);

  return (
    <div className={`${baseStyle} ${typeStyle}`}>
      {message}
    </div>
  );
};

// --- PÁGINAS DA APLICAÇÃO ---

// Página de Login
const LoginPage = ({ onLoginSuccess, onNavigate }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      const response = await apiService.login(email, password);
      if (response.ok) {
        const data = await response.json();
        onLoginSuccess(data.token);
      } else {
        const errorData = await response.text();
        setError('Credenciais inválidas. Verifique seu e-mail e senha.');
        console.error('Falha no login:', errorData);
      }
    } catch (err) {
      setError('Não foi possível conectar ao servidor. Tente novamente mais tarde.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md">
        <div className="flex justify-center mb-6">
          <MilkIcon />
          <h1 className="text-2xl font-bold text-gray-800 ml-2">Serra Mineira Laticínios</h1>
        </div>
        <h2 className="text-xl text-center font-semibold text-gray-700 mb-6">Acessar o Sistema</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-600 mb-2" htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          <div className="mb-6">
            <label className="block text-gray-600 mb-2" htmlFor="password">Senha</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
          {error && <p className="text-red-500 text-sm mb-4 text-center">{error}</p>}
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-300 disabled:bg-blue-300"
            disabled={isLoading}
          >
            {isLoading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>
        <p className="text-center text-gray-600 mt-6">
          Não tem uma conta?{' '}
          <button onClick={() => onNavigate('register')} className="text-blue-600 hover:underline">
            Registre-se
          </button>
        </p>
      </div>
    </div>
  );
};

// Página de Registro
const RegisterPage = ({ onNavigate, setNotification }) => {
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);
        try {
            const response = await apiService.register(nome, email, password);
            if (response.status === 201) {
                setNotification({ message: 'Usuário registrado com sucesso! Faça o login.', type: 'success' });
                onNavigate('login');
            } else {
                setError('Não foi possível registrar. O e-mail pode já estar em uso.');
            }
        } catch (err) {
            setError('Erro de conexão. Tente novamente.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md">
                <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">Criar Nova Conta</h1>
                <form onSubmit={handleSubmit}>
                    <div className="mb-4">
                        <label className="block text-gray-600 mb-2" htmlFor="nome">Nome Completo</label>
                        <input type="text" id="nome" value={nome} onChange={(e) => setNome(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                    </div>
                    <div className="mb-4">
                        <label className="block text-gray-600 mb-2" htmlFor="email-register">Email</label>
                        <input type="email" id="email-register" value={email} onChange={(e) => setEmail(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                    </div>
                    <div className="mb-6">
                        <label className="block text-gray-600 mb-2" htmlFor="password-register">Senha</label>
                        <input type="password" id="password-register" value={password} onChange={(e) => setPassword(e.target.value)} className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                    </div>
                    {error && <p className="text-red-500 text-sm mb-4 text-center">{error}</p>}
                    <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-300 disabled:bg-blue-300" disabled={isLoading}>
                        {isLoading ? 'Registrando...' : 'Registrar'}
                    </button>
                </form>
                <p className="text-center text-gray-600 mt-6">
                    Já tem uma conta?{' '}
                    <button onClick={() => onNavigate('login')} className="text-blue-600 hover:underline">
                        Faça o login
                    </button>
                </p>
            </div>
        </div>
    );
};

// Página do Dashboard (Página principal após o login)
const DashboardPage = () => {
    // Dados de exemplo para os cards e gráfico
    const summaryData = [
        { title: "Produtores Ativos", value: "42", icon: <UsersIcon />, color: "blue" },
        { title: "Litros Coletados (Mês)", value: "125.840 L", icon: <MilkIcon />, color: "green" },
        { title: "Pagamentos Pendentes", value: "R$ 15.230,50", icon: <CalendarIcon />, color: "orange" },
        { title: "Coletas Hoje", value: "3.500 L", icon: <ClipboardIcon />, color: "indigo" },
    ];

    const chartData = [
        { name: 'Jan', Produção: 4000 },
        { name: 'Fev', Produção: 3000 },
        { name: 'Mar', Produção: 5000 },
        { name: 'Abr', Produção: 4500 },
        { name: 'Mai', Produção: 6000 },
        { name: 'Jun', Produção: 5500 },
    ];

    return (
        <div>
            <h1 className="text-3xl font-bold text-gray-800 mb-6">Dashboard</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                {summaryData.map(item => (
                    <div key={item.title} className={`bg-white p-6 rounded-xl shadow-md flex items-center border-l-4 border-${item.color}-500`}>
                        <div className={`p-3 rounded-full bg-${item.color}-100 text-${item.color}-600 mr-4`}>
                            {item.icon}
                        </div>
                        <div>
                            <p className="text-gray-500 text-sm">{item.title}</p>
                            <p className="text-2xl font-bold text-gray-800">{item.value}</p>
                        </div>
                    </div>
                ))}
            </div>
            
            <div className="bg-white p-6 rounded-xl shadow-md">
                <h2 className="text-xl font-semibold text-gray-700 mb-4">Produção Semestral (Litros)</h2>
                <div style={{ width: '100%', height: 300 }}>
                     <ResponsiveContainer>
                        <BarChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="Produção" fill="#3b82f6" />
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </div>
        </div>
    );
};

// Páginas de placeholder para outras seções
const PlaceholderPage = ({ title }) => (
    <div>
        <h1 className="text-3xl font-bold text-gray-800 mb-6">{title}</h1>
        <div className="bg-white p-8 rounded-xl shadow-md text-center">
            <p className="text-gray-600">Conteúdo da página de {title} será implementado aqui.</p>
            <button className="mt-4 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
                Adicionar Novo
            </button>
        </div>
    </div>
);


// --- ESTRUTURA PRINCIPAL DO DASHBOARD ---

const MainLayout = ({ currentPage, onNavigate, onLogout }) => {
    const navItems = [
        { id: 'dashboard', label: 'Dashboard', icon: <DashboardIcon /> },
        { id: 'producers', label: 'Produtores', icon: <UsersIcon /> },
        { id: 'collections', label: 'Coletas', icon: <ClipboardIcon /> },
        { id: 'closings', label: 'Fechamentos', icon: <CalendarIcon /> },
        { id: 'reports', label: 'Relatórios', icon: <ChartIcon /> },
    ];

    const renderPage = () => {
        switch (currentPage) {
            case 'dashboard': return <DashboardPage />;
            case 'producers': return <PlaceholderPage title="Gestão de Produtores" />;
            case 'collections': return <PlaceholderPage title="Lançamento de Coletas" />;
            case 'closings': return <PlaceholderPage title="Fechamentos Mensais" />;
            case 'reports': return <PlaceholderPage title="Relatórios e Gráficos" />;
            default: return <DashboardPage />;
        }
    };

    return (
        <div className="flex h-screen bg-gray-100">
            {/* Sidebar (Menu Lateral) */}
            <aside className="w-64 bg-gray-800 text-white flex flex-col">
                <div className="h-16 flex items-center justify-center border-b border-gray-700">
                    <MilkIcon />
                    <h1 className="text-xl font-bold ml-2">SMLaticínios</h1>
                </div>
                <nav className="flex-1 px-4 py-6">
                    <ul>
                        {navItems.map(item => (
                             <li key={item.id}>
                                <button 
                                    onClick={() => onNavigate(item.id)}
                                    className={`w-full flex items-center px-4 py-3 rounded-lg transition duration-200 ${currentPage === item.id ? 'bg-blue-600' : 'hover:bg-gray-700'}`}
                                >
                                    {item.icon}
                                    <span className="ml-4">{item.label}</span>
                                </button>
                            </li>
                        ))}
                    </ul>
                </nav>
                <div className="p-4 border-t border-gray-700">
                     <button onClick={onLogout} className="w-full flex items-center px-4 py-3 rounded-lg hover:bg-gray-700 transition duration-200">
                        <LogoutIcon />
                        <span className="ml-4">Sair</span>
                    </button>
                </div>
            </aside>
            
            {/* Conteúdo Principal */}
            <div className="flex-1 flex flex-col overflow-hidden">
                {/* Header */}
                <header className="bg-white shadow-md h-16 flex items-center justify-between px-6">
                    <h2 className="text-lg font-semibold text-gray-700">Bem-vindo, Usuário!</h2>
                    {/* Aqui podem ir outros itens como perfil do usuário, notificações, etc. */}
                </header>
                {/* Área de conteúdo da página */}
                <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100 p-6">
                    {renderPage()}
                </main>
            </div>
        </div>
    );
};


// --- COMPONENTE PRINCIPAL (App) ---
export default function App() {
  // Tenta pegar o token do localStorage ao iniciar
  const [token, setToken] = useState(localStorage.getItem('authToken'));
  // Página inicial é o login se não houver token, senão é o dashboard
  const [currentPage, setCurrentPage] = useState(token ? 'dashboard' : 'login');
  const [notification, setNotification] = useState({ message: '', type: '' });

  // Efeito para salvar/remover o token do localStorage quando ele mudar
  useEffect(() => {
    if (token) {
      localStorage.setItem('authToken', token);
    } else {
      localStorage.removeItem('authToken');
    }
  }, [token]);

  // Função chamada quando o login é bem-sucedido
  const handleLoginSuccess = (newToken) => {
    setToken(newToken);
    setCurrentPage('dashboard');
  };

  // Função para logout
  const handleLogout = () => {
    setToken(null);
    setCurrentPage('login');
    setNotification({ message: 'Você saiu do sistema.', type: 'success' });
  };

  // Função para navegação entre páginas
  const handleNavigate = (page) => {
    setCurrentPage(page);
  };

  const handleDismissNotification = () => {
    setNotification({ message: '', type: '' });
  };

  // Renderiza a aplicação
  return (
    <div>
      <Notification message={notification.message} type={notification.type} onDismiss={handleDismissNotification} />
      {token ? (
        <MainLayout currentPage={currentPage} onNavigate={handleNavigate} onLogout={handleLogout} />
      ) : (
        <>
          {currentPage === 'register' ? (
            <RegisterPage onNavigate={handleNavigate} setNotification={setNotification} />
          ) : (
            <LoginPage onLoginSuccess={handleLoginSuccess} onNavigate={handleNavigate} />
          )}
        </>
      )}
    </div>
  );
}
