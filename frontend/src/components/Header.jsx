import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaBars, FaTimes, FaUser, FaNewspaper, FaCalendarAlt, FaUsers, FaSignOutAlt } from 'react-icons/fa';
import './Header.css';

function Header() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const [scrolled, setScrolled] = useState(false);

    useEffect(() => {
        const handleScroll = () => {
            const isScrolled = window.scrollY > 50;
            if (isScrolled !== scrolled) {
                setScrolled(isScrolled);
            }
        };

        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, [scrolled]);

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <header className={`header ${scrolled ? 'scrolled' : ''}`}>
            <div className="header-container">
                <div className="header-left">
                    <div className="logo" onClick={() => navigate('/')}>
                        <span className="logo-text">ШИКАНА</span>
                        <div className="logo-line"></div>
                    </div>
                </div>

                <div className="header-center">
                    <nav className="desktop-nav">
                        <button onClick={() => navigate('/news')} className="nav-item">
                            <FaNewspaper className="nav-icon" />
                            <span>Новости</span>
                        </button>
                        <button onClick={() => navigate('/')} className="nav-item">
                            <FaCalendarAlt className="nav-icon" />
                            <span>События</span>
                        </button>
                        <button onClick={() => navigate('/pilots')} className="nav-item">
                            <FaUsers className="nav-icon" />
                            <span>Пилоты</span>
                        </button>
                    </nav>
                </div>

                <div className="header-right">
                    {token ? (
                        <div className="user-menu">
                            <button onClick={() => navigate('/profile')} className="profile-button">
                                <FaUser className="profile-icon" />
                                <span>Профиль</span>
                            </button>
                            <button onClick={handleLogout} className="logout-button">
                                <FaSignOutAlt className="logout-icon" />
                                <span>Выйти</span>
                            </button>
                        </div>
                    ) : (
                        <button onClick={() => navigate('/login')} className="login-button">
                            <FaUser className="login-icon" />
                            <span>Вход</span>
                        </button>
                    )}

                    <button onClick={toggleMenu} className="hamburger">
                        {isMenuOpen ? <FaTimes /> : <FaBars />}
                    </button>
                </div>
            </div>

            {isMenuOpen && (
                <div className="mobile-menu">
                    <nav className="mobile-nav">
                        <button onClick={() => { navigate('/news'); setIsMenuOpen(false); }} className="mobile-nav-item">
                            <FaNewspaper className="mobile-nav-icon" />
                            <span>Новости</span>
                        </button>
                        <button onClick={() => { navigate('/'); setIsMenuOpen(false); }} className="mobile-nav-item">
                            <FaCalendarAlt className="mobile-nav-icon" />
                            <span>События</span>
                        </button>
                        <button onClick={() => { navigate('/pilots'); setIsMenuOpen(false); }} className="mobile-nav-item">
                            <FaUsers className="mobile-nav-icon" />
                            <span>Пилоты</span>
                        </button>
                        {token ? (
                            <>
                                <button onClick={() => { navigate('/profile'); setIsMenuOpen(false); }} className="mobile-nav-item">
                                    <FaUser className="mobile-nav-icon" />
                                    <span>Профиль</span>
                                </button>
                                <button onClick={() => { handleLogout(); setIsMenuOpen(false); }} className="mobile-nav-item">
                                    <FaSignOutAlt className="mobile-nav-icon" />
                                    <span>Выйти</span>
                                </button>
                            </>
                        ) : (
                            <button onClick={() => { navigate('/login'); setIsMenuOpen(false); }} className="mobile-nav-item">
                                <FaUser className="mobile-nav-icon" />
                                <span>Вход</span>
                            </button>
                        )}
                    </nav>
                </div>
            )}
        </header>
    );
}

export default Header;