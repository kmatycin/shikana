import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaBars } from 'react-icons/fa'; // Импортируем иконку для сендвича

function Header() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <header className="header">
            <div className="header-container">
                {/* Логотип */}
                <div className="logo" onClick={() => navigate('/')}>
                    ШИКАНА
                </div>

                {/* Сендвич-иконка (hamburger) */}
                <button onClick={toggleMenu} className="hamburger">
                    <FaBars className="hamburger-icon" />
                </button>
            </div>

            {/* Выпадающее меню */}
            {isMenuOpen && (
                <div className="menu">
                    <ul className="menu-list">
                        <li>
                            <button onClick={() => { navigate('/news'); setIsMenuOpen(false); }} className="menu-item">
                                Новости
                            </button>
                        </li>
                        <li>
                            <button onClick={() => { navigate('/'); setIsMenuOpen(false); }} className="menu-item">
                                События
                            </button>
                        </li>
                        <li>
                            <button onClick={() => { navigate('/pilots'); setIsMenuOpen(false); }} className="menu-item">
                                Пилоты
                            </button>
                        </li>
                        <li>
                            <button onClick={() => { navigate('/profile'); setIsMenuOpen(false); }} className="menu-item">
                                Профиль
                            </button>
                        </li>
                        {token ? (
                            <li>
                                <button onClick={() => { handleLogout(); setIsMenuOpen(false); }} className="menu-item logout-button">
                                    Выйти
                                </button>
                            </li>
                        ) : (
                            <li>
                                <button onClick={() => { navigate('/login'); setIsMenuOpen(false); }} className="menu-item login-button">
                                    Вход
                                </button>
                            </li>
                        )}
                    </ul>
                </div>
            )}
        </header>
    );
}

export default Header;