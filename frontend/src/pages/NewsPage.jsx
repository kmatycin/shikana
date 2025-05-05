import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './NewsPage.css'; // Подключим новые стили

function NewsPage() {
    const [news, setNews] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        if (!token || !token.includes('.')) {
            navigate('/login');
            return;
        }

        setLoading(true);

        // Запрос только на /api/news
        axios
            .get('/api/news', { headers: { Authorization: `Bearer ${token}` } })
            .then((response) => {
                // Если API возвращает массив — используем его напрямую
                // Если API возвращает объект с полем data — используем response.data.data
                const data = Array.isArray(response.data) ? response.data : response.data.data;
                setNews(data || []);
            })
            .catch((err) => {
                setError('Ошибка загрузки новостей: ' + (err.response?.data?.message || 'Попробуйте снова'));
                if (err.response?.status === 401) navigate('/login');
            })
            .finally(() => setLoading(false));
    }, [token, navigate]);

    if (loading) {
        return <div>Загрузка новостей...</div>;
    }

    if (!news.length) {
        return <div>Нет новостей</div>;
    }

    return (
        <div className="news-page">
            <h1 className="news-title">Новости</h1>
            {error && <p className="error-message">{error}</p>}

            {/* Лента новостей */}
            <div className="news-feed">
                {news.map((item) => (
                    <div key={item.id} className="news-card">
                        {item.imageUrl && (
                            <div className="news-card-image-wrapper">
                                <img
                                    src={item.imageUrl.startsWith('http') ? item.imageUrl : `https://yoklmnracing.ru${item.imageUrl}`}
                                    className="news-card-image"
                                    alt={item.title}
                                />
                            </div>
                        )}
                        <div className="news-card-content">
                            <h2 className="news-card-title">{item.title}</h2>
                            <p className="news-card-text">{item.content}</p>
                            {item.date && (
                                <p className="news-card-date">
                                    <span>{new Date(item.date).toLocaleDateString()}</span>
                                </p>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default NewsPage;