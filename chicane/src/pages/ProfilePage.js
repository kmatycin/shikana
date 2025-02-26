import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ProfilePage() {
    const [profile, setProfile] = useState(null);
    const token = 'your-jwt-token-here'; // Замените на реальный токен
    const userId = 'your-user-id-here'; // Замените на реальный ID

    useEffect(() => {
        axios.get(`/api/profiles/${userId}`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setProfile(response.data.data))
            .catch(error => console.error('Ошибка загрузки профиля:', error));
    }, []);

    if (!profile) return <div>Загрузка...</div>;

    return (
        <div>
            <h1>Профиль</h1>
            <p>Имя: {profile.username}</p>
            <p>Роль: {profile.role}</p>
            <p>Био: {profile.bio || 'Не указано'}</p>
            {profile.avatarUrl && <img src={profile.avatarUrl} alt="Аватар" width="100" />}
        </div>
    );
}

export default ProfilePage;