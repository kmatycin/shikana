import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './RegisterPage.css'; // Подключаем стили, схожие с LoginPage.css

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('SPECTATOR');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/auth/register', {
                username,
                email,
                password,
                role,
            });
            let token = response.data.data.token.replace('Bearer ', '').trim();
            console.log('Сохраняемый токен:', token);
            localStorage.setItem('token', token);
            setError('');
            navigate('/');
        } catch (err) {
            setError('Ошибка регистрации: ' + (err.response?.data?.message || 'Попробуйте снова'));
        }
    };

    const handleLoginClick = () => {
        navigate('/login');
    };

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };

    return (
        <div className="register-page">
            <div className="register-content">
                <div className="register-form">
                    <h1 className="register-title">Регистрация</h1>
                    {error && <p className="error-message">{error}</p>}
                    <div className="input-group">
                        <div className="input-field">
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="input"
                                placeholder="Имя пользователя"
                            />
                        </div>
                        <div className="input-field">
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="input"
                                placeholder="Email"
                            />
                        </div>
                        <div className="input-field">
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="input"
                                placeholder="Пароль"
                            />
                        </div>
                        <div className="input-field">
                            <label className="input-label">Роль</label>
                            <select
                                value={role}
                                onChange={(e) => setRole(e.target.value)}
                                className="input select"
                            >
                                <option value="SPECTATOR">Зритель</option>
                                <option value="PILOT">Пилот</option>
                                <option value="ORGANIZER">Организатор</option>
                            </select>
                        </div>
                    </div>
                    <button onClick={handleRegister} className="sign-in-button">
                        Создать
                    </button>
                    <div className="register-link">
                        <span className="no-account">Уже есть аккаунт?</span>
                        <button onClick={handleLoginClick} className="create-button">
                            Войти
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;