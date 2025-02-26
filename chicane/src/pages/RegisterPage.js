import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('SPECTATOR');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/auth/register', {
                username,
                email,
                password,
                role,
            });
            const token = response.data.token; // Предполагаем, что бэкенд возвращает токен
            localStorage.setItem('token', token);
            setError('');
            navigate('/'); // Перенаправляем на главную
        } catch (err) {
            setError('Ошибка регистрации: ' + (err.response?.data?.message || 'Попробуйте снова'));
        }
    };

    return (
        <div className="auth-container">
            <h1>Регистрация</h1>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleRegister}>
                <div>
                    <label>Имя пользователя:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
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
                <div>
                    <label>Роль:</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="SPECTATOR">Зритель</option>
                        <option value="PILOT">Пилот</option>
                        <option value="ORGANIZER">Организатор</option>
                    </select>
                </div>
                <button type="submit">Зарегистрироваться</button>
            </form>
            <p>
                Уже есть аккаунт? <a href="/login">Войдите</a>
            </p>
        </div>
    );
}

export default RegisterPage;