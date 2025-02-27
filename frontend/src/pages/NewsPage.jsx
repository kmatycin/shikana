import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './NewsPage.css'; // Подключим новые стили

function NewsPage() {
    const [news, setNews] = useState([]);
    const [content, setContent] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();
    const [userRole, setUserRole] = useState(null);

    useEffect(() => {
        if (!token || !token.includes('.')) {
            navigate('/login');
            return;
        }

        setLoading(true);
        axios
            .get('/api/news', { headers: { Authorization: `Bearer ${token}` } })
            .then((response) => setNews(response.data.data || []))
            .catch((err) => {
                setError('Ошибка загрузки новостей: ' + (err.response?.data?.message || 'Попробуйте снова'));
                if (err.response?.status === 401) navigate('/login');
            })
            .finally(() => setLoading(false));

        axios
            .get('/api/profiles/me', { headers: { Authorization: `Bearer ${token}` } })
            .then((response) => setUserRole(response.data.data.role))
            .catch(() => setUserRole('SPECTATOR'));
    }, [token, navigate]);

    const handleCreateNews = async (e) => {
        e.preventDefault();
        if (!content.trim()) {
            setError('Введите текст новости');
            return;
        }

        setLoading(true);
        try {
            const response = await axios.post(
                '/api/news',
                { content },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            const newNewsItem = response.data.data;
            setNews([newNewsItem, ...news]);
            setContent('');
            setError('');
        } catch (err) {
            setError('Ошибка создания новости: ' + (err.response?.data?.message || 'Попробуйте снова'));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="news-page">
            <h1 className="news-title">Новости</h1>
            {error && <p className="error-message">{error}</p>}

            {/* Форма создания новости */}
            {userRole && userRole !== 'SPECTATOR' && (
                <div className="create-post-container">
                    <div className="create-post">
                        <div className="input-wrapper">
                            <img
                                src="https://via.placeholder.com/20" // Заглушка для иконки
                                alt="News Icon"
                                className="input-icon"
                            />
                            <form onSubmit={handleCreateNews} className="post-form">
                <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Напиши, что у тебя нового?"
                    className="post-input"
                    rows="2"
                    disabled={loading}
                />
                            </form>
                        </div>
                        <button type="submit" className="post-button" disabled={loading}>
                            {loading ? 'Публикуется...' : 'Опубликовать'}
                        </button>
                    </div>
                </div>
            )}

            {/* Лента новостей */}
            <div className="news-feed">
                {loading && <p className="loading">Загрузка...</p>}
                {!loading && news.length === 0 && <p className="no-posts">Новостей пока нет</p>}
                {news.map((item) => (
                    <div key={item.id} className="news-card">
                        <div className="news-header">
                            <img
                                src="https://via.placeholder.com/40" // Заглушка для аватара
                                alt="Author Avatar"
                                className="avatar"
                            />
                            <div className="news-info">
                                <span className="author-name">{item.authorName || 'Автор'}</span>
                                <span className="post-time">
                  {new Date(item.createdAt).toLocaleString()}
                </span>
                            </div>
                        </div>
                        <p className="news-content">{item.content}</p>
                        <div className="news-actions">
                            <button className="action-button">
                                <span>Лайк</span>
                            </button>
                            <button className="action-button">
                                <span>Комментарий</span>
                            </button>
                            <button className="action-button">
                                <span>Репост</span>
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default NewsPage;