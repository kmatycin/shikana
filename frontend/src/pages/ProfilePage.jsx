import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function ProfilePage() {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        console.log('Токен перед очисткой:', token);
        const cleanedToken = token ? token.trim() : null;
        console.log('Очищенный токен:', cleanedToken);
        const authHeader = `Bearer ${cleanedToken}`; // Без лишних пробелов
        console.log('Заголовок Authorization:', authHeader);

        if (!cleanedToken || !cleanedToken.includes('.')) {
            console.warn('Некорректный токен:', cleanedToken);
            localStorage.removeItem('token');
            navigate('/login');
            return;
        }

        axios
            .get('/api/profiles/me', {
                headers: { Authorization: authHeader },
            })
            .then((response) => {
                console.log('Ответ от /api/profiles/me:', response.data);
                setProfile(response.data.data);
            })
            .catch((err) => {
                console.error('Ошибка /api/profiles/me:', err.response?.status, err.response?.data);
                setError('Ошибка загрузки профиля: ' + (err.response?.data?.message || 'Попробуйте снова'));
                if (err.response?.status === 401) {
                    localStorage.removeItem('token');
                    navigate('/login');
                }
            });
    }, [token, navigate]);

    if (!profile) return <div>{error || 'Загрузка...'}</div>;

    return (
        <div>
            <h1>Профиль</h1>
            {error && <p className="error">{error}</p>}
            <p>Имя: {profile.username}</p>
            <p>Роль: {profile.role}</p>
            <p>Био: {profile.bio || 'Не указано'}</p>
            {profile.avatarUrl && <img src={profile.avatarUrl} alt="Аватар" width="100" />}
        </div>
    );
}

export default ProfilePage;