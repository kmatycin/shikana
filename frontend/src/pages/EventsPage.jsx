import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './EventsPage.css';

function EventsPage() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }

        setLoading(true);

        // Fetch both internal and external events
        Promise.all([
            axios.get('/api/events', {
                headers: { Authorization: `Bearer ${token}` }
            }),
            axios.get('/api/events/external', {
                headers: { Authorization: `Bearer ${token}` }
            })
        ])
            .then(([internalRes, externalRes]) => {
                // Combine and transform events
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
            })
            .catch(err => {
                setError('Ошибка загрузки событий: ' + (err.response?.data?.message || 'Попробуйте снова'));
                if (err.response?.status === 401) navigate('/login');
            })
            .finally(() => setLoading(false));
    }, [token, navigate]);

    if (loading) return <div className="loading">Загрузка событий...</div>;

    return (
        <div className="events-container">
            <h1 className="events-title">Все события</h1>
            {error && <p className="error-message">{error}</p>}

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