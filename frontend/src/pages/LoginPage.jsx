import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/auth/login', { email, password });
            console.log('Ответ от бэкенда:', response.data);
            let token = response.data.data.token; // Токен внутри data
            if (!token) {
                throw new Error('Токен не получен от сервера');
            }
            // Убираем префикс "Bearer " если он есть
            token = token.replace('Bearer ', '');
            console.log('Сохраняемый токен:', token);
            localStorage.setItem('token', token);
            setError('');
            navigate('/');
        } catch (err) {
            setError('Ошибка входа: ' + (err.response?.data?.message || err.message || 'Попробуйте снова'));
        }
    };

    return (
        <div className="auth-container">
            <h1>Вход</h1>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleLogin}>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Пароль:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Войти</button>
            </form>
            <p>
                Нет аккаунта? <a href="/register">Зарегистрируйтесь</a>
            </p>
        </div>
    );
}

export default LoginPage;