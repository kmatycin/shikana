import React, { useState, useRef, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const videoRef = useRef(null); // Ссылка на видео для управления

    useEffect(() => {
        // Автоматическое воспроизведение видео при загрузке
        if (videoRef.current) {
            videoRef.current.play().catch((error) => {
                console.error('Ошибка автоплей видео:', error);
            });
        }
    }, []);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/auth/login', { email, password });
            let token = response.data.data.token.replace('Bearer ', '').trim();
            console.log('Сохраняемый токен:', token);
            localStorage.setItem('token', token);
            setError('');
            navigate('/');
        } catch (err) {
            setError('Ошибка входа: ' + (err.response?.data?.message || 'Попробуйте снова'));
        }
    };

    const handleRegisterClick = () => {
        navigate('/register');
    };



    return (
        <div className="login-page">
                <div className="login-form">
                    <h1 className="login-title">Вход</h1>
                    {error && <p className="error-message">{error}</p>}
                    <div className="input-group">
                        <div className="input-field">
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="input"
                                placeholder="email"
                            />
                        </div>
                        <div className="input-field">
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="input"
                                placeholder="пароль"
                            />
                        </div>
                    </div>
                    <button onClick={handleLogin} className="sign-in-button">
                        Войти
                    </button>
                    <div className="register-link">
                        <span className="no-account">Нет Аккаунта?</span>
                        <button onClick={handleRegisterClick} className="create-button">
                            Создать
                        </button>
                    </div>
                </div>

            </div>

    );
}

export default LoginPage;