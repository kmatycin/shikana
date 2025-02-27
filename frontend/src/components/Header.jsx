import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FaSignOutAlt } from 'react-icons/fa'; // Для иконки выхода (опционально)

function Header() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <header className="header">
            <div className="header-container">
                {/* Логотип */}
                <div className="logo" onClick={() => navigate('/')}>
                    <h1 className="logo-text">MotorsportHub</h1>
                </div>

                {/* Навигация и действия */}
                <div className="nav-actions">
                    <nav className="nav">
                        <ul className="nav-list">
                            <li>
                                <button onClick={() => navigate('/')} className="nav-button">
                                    События
                                </button>
                            </li>
                            <li>
                                <button onClick={() => navigate('/news')} className="nav-button">
                                    Новости
                                </button>
                            </li>
                            <li>
                                <button onClick={() => navigate('/profile')} className="nav-button">
                                    Профиль
                                </button>
                            </li>
                        </ul>
                    </nav>
                    {token ? (
                        <button onClick={handleLogout} className="nav-button logout-button">
                            <FaSignOutAlt className="inline mr-1" /> Выйти
                        </button>
                    ) : (
                        <button onClick={() => navigate('/login')} className="nav-button login-button">
                            Вход
                        </button>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;