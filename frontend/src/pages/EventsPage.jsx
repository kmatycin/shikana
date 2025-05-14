import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './EventsPage.css';

function EventsPage() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        date: '',
        location: '',
        imageUrl: '',
        isExternal: false
    });
    const [formError, setFormError] = useState('');
    const [formLoading, setFormLoading] = useState(false);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }

        fetchEvents();
    }, [token, navigate]);

    const fetchEvents = async () => {
        setLoading(true);
        try {
            const [internalRes, externalRes] = await Promise.all([
                axios.get('/api/events', {
                    headers: { Authorization: `Bearer ${token}` }
                }),
                axios.get('/api/events/external', {
                    headers: { Authorization: `Bearer ${token}` }
                })
            ]);

            const combinedEvents = [
                ...(internalRes.data?.data || []).map(e => ({
                    ...e,
                    isExternal: false,
                    date: e.date ? new Date(e.date) : null
                })),
                ...(externalRes.data?.data || []).map(e => ({
                    ...e,
                    isExternal: true,
                    date: e.date ? new Date(e.date) : null
                }))
            ];
            setEvents(combinedEvents);
        } catch (err) {
            setError('Ошибка загрузки событий: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        } finally {
            setLoading(false);
        }
    };

    const handleFormChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        setFormLoading(true);
        setFormError('');

        try {
            const endpoint = formData.isExternal ? '/api/events/external' : '/api/events';
            await axios.post(endpoint, formData, {
                headers: { Authorization: `Bearer ${token}` }
            });
            // Очищаем форму и обновляем список событий
            setFormData({
                title: '',
                description: '',
                date: '',
                location: '',
                imageUrl: '',
                isExternal: false
            });
            setShowForm(false); // Скрываем форму после успешного создания
            await fetchEvents();
        } catch (err) {
            setFormError('Ошибка при создании события: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        } finally {
            setFormLoading(false);
        }
    };

    if (loading) return <div className="loading">Загрузка событий...</div>;

    return (
        <div className="events-container">
            <div className="events-header">
                <h1 className="events-title">Все события</h1>
                <button 
                    className="create-event-button"
                    onClick={() => setShowForm(!showForm)}
                >
                    {showForm ? 'Отмена' : 'Создать событие'}
                </button>
            </div>
            {error && <p className="error-message">{error}</p>}

            {showForm && (
                <div className="event-form-container">
                    <h2>Создание нового события</h2>
                    {formError && <p className="error-message">{formError}</p>}
                    
                    <form onSubmit={handleFormSubmit} className="event-form">
                        <div className="form-group">
                            <label htmlFor="title">Название события *</label>
                            <input
                                type="text"
                                id="title"
                                name="title"
                                value={formData.title}
                                onChange={handleFormChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="description">Описание</label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleFormChange}
                                rows="4"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="date">Дата события *</label>
                            <input
                                type="datetime-local"
                                id="date"
                                name="date"
                                value={formData.date}
                                onChange={handleFormChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="location">Место проведения</label>
                            <input
                                type="text"
                                id="location"
                                name="location"
                                value={formData.location}
                                onChange={handleFormChange}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="imageUrl">URL изображения</label>
                            <input
                                type="url"
                                id="imageUrl"
                                name="imageUrl"
                                value={formData.imageUrl}
                                onChange={handleFormChange}
                                placeholder="https://example.com/image.jpg"
                            />
                        </div>

                        <div className="form-group checkbox">
                            <label>
                                <input
                                    type="checkbox"
                                    name="isExternal"
                                    checked={formData.isExternal}
                                    onChange={handleFormChange}
                                />
                                Внешнее событие
                            </label>
                        </div>

                        <button 
                            type="submit" 
                            className="submit-button"
                            disabled={formLoading}
                        >
                            {formLoading ? 'Создание...' : 'Создать событие'}
                        </button>
                    </form>
                </div>
            )}

            <div className="events-grid">
                {events.map(event => (
                    <div key={event.id || event.title} className={`event-card ${event.isExternal ? 'external' : ''}`}>
                        {event.imageUrl && (
                            <div className="event-image-container">
                                <img
                                    src={event.imageUrl.startsWith('http')
                                        ? event.imageUrl
                                        : `[https://yoklmnracing.ru](https://yoklmnracing.ru)${event.imageUrl}`}
                                    alt={event.title}
                                    className="event-image"
                                />
                                {event.isExternal && (
                                    <span className="external-badge">Внешнее</span>
                                )}
                            </div>
                        )}
                        <div className="event-content">
                            <h3 className="event-title">{event.title}</h3>
                            {event.description && (
                                <p className="event-description">{event.description}</p>
                            )}
                            <div className="event-details">
                                {event.date && (
                                    <p className="event-date">
                                        {event.date.toLocaleDateString('ru-RU', {
                                            day: 'numeric',
                                            month: 'long',
                                            year: 'numeric'
                                        })}
                                    </p>
                                )}
                                {event.location && (
                                    <p className="event-location">{event.location}</p>
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default EventsPage;