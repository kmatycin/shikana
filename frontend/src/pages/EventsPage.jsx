import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import UserMention from '../components/UserMention';
import './EventsPage.css';

function EventsPage() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingEvent, setEditingEvent] = useState(null);
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        date: '',
        location: '',
        imageUrl: '',
        game: '',
        stages: '',
        organizer: ''
    });
    const [mentionSearch, setMentionSearch] = useState('');
    const [mentionResults, setMentionResults] = useState([]);
    const [showMentionResults, setShowMentionResults] = useState(false);
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
            const response = await axios.get('/api/events', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setEvents(response.data.data.content.map(event => ({
                ...event,
                date: event.date ? new Date(event.date) : null
            })));
        } catch (err) {
            setError('Ошибка загрузки событий: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // Handle mention search
        if (name === 'description') {
            const lastAtSymbol = value.lastIndexOf('@');
            if (lastAtSymbol !== -1) {
                const searchText = value.slice(lastAtSymbol + 1);
                if (searchText.includes(' ')) {
                    setShowMentionResults(false);
                } else {
                    setMentionSearch(searchText);
                    searchUsers(searchText);
                }
            } else {
                setShowMentionResults(false);
            }
        }
    };

    const searchUsers = async (query) => {
        if (!query) {
            setMentionResults([]);
            setShowMentionResults(false);
            return;
        }

        try {
            const response = await axios.get(`/api/pilots/search?query=${query}`);
            setMentionResults(response.data.map(pilot => ({
                username: pilot.nickname,
                avatarUrl: pilot.photoUrl
            })));
            setShowMentionResults(true);
        } catch (err) {
            console.error('Failed to search pilots:', err);
        }
    };

    const insertMention = (username, avatarUrl) => {
        const lastAtSymbol = formData.description.lastIndexOf('@');
        const newDescription = formData.description.slice(0, lastAtSymbol) + 
            `@${username} ` + 
            formData.description.slice(lastAtSymbol + mentionSearch.length + 1);
        
        setFormData(prev => ({
            ...prev,
            description: newDescription
        }));
        setShowMentionResults(false);
    };

    const parseMentions = (text) => {
        const mentionRegex = /@(\w+)/g;
        const parts = [];
        let lastIndex = 0;
        let match;

        while ((match = mentionRegex.exec(text)) !== null) {
            // Add text before mention
            if (match.index > lastIndex) {
                parts.push(text.slice(lastIndex, match.index));
            }

            // Add mention component
            parts.push(
                <UserMention
                    key={match.index}
                    username={match[1]}
                />
            );

            lastIndex = match.index + match[0].length;
        }

        // Add remaining text
        if (lastIndex < text.length) {
            parts.push(text.slice(lastIndex));
        }

        return parts;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingEvent) {
                await axios.put(`/api/events/${editingEvent.id}`, formData, {
                    headers: { Authorization: `Bearer ${token}` }
                });
            } else {
                await axios.post('/api/events', formData, {
                    headers: { Authorization: `Bearer ${token}` }
                });
            }
            fetchEvents();
            resetForm();
        } catch (err) {
            setError('Ошибка при сохранении события: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const handleEdit = (event) => {
        setEditingEvent(event);
        setFormData({
            title: event.title,
            description: event.description,
            date: event.date ? new Date(event.date).toISOString().slice(0, 16) : '',
            location: event.location,
            imageUrl: event.imageUrl,
            game: event.game,
            stages: event.stages,
            organizer: event.organizer
        });
        setShowForm(true);
    };

    const handleDelete = async (eventId) => {
        if (!window.confirm('Вы уверены, что хотите удалить это событие?')) {
            return;
        }

        try {
            await axios.delete(`/api/events/${eventId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            await fetchEvents();
        } catch (err) {
            setError('Ошибка при удалении события: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const resetForm = () => {
        setFormData({
            title: '',
            description: '',
            date: '',
            location: '',
            imageUrl: '',
            game: '',
            stages: '',
            organizer: ''
        });
        setEditingEvent(null);
        setShowForm(false);
    };

    if (loading) return <div className="loading">Загрузка событий...</div>;

    return (
        <div className="events-container">
            <div className="events-header">
                <h1 className="events-title">Все события</h1>
                <button 
                    className="create-event-button"
                    onClick={() => {
                        if (showForm) {
                            resetForm();
                        } else {
                            setShowForm(true);
                        }
                    }}
                >
                    {showForm ? 'Отмена' : 'Создать событие'}
                </button>
            </div>
            {error && <p className="error-message">{error}</p>}

            {showForm && (
                <div className="event-form-container">
                    <h2>{editingEvent ? 'Редактирование события' : 'Создание нового события'}</h2>
                    
                    <form onSubmit={handleSubmit} className="event-form">
                        <div className="form-group">
                            <label htmlFor="title">Название события *</label>
                            <input
                                type="text"
                                id="title"
                                name="title"
                                value={formData.title}
                                onChange={handleInputChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="description">Описание</label>
                            <textarea
                                id="description"
                                name="description"
                                value={formData.description}
                                onChange={handleInputChange}
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
                                onChange={handleInputChange}
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
                                onChange={handleInputChange}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="imageUrl">URL изображения</label>
                            <input
                                type="url"
                                id="imageUrl"
                                name="imageUrl"
                                value={formData.imageUrl}
                                onChange={handleInputChange}
                                placeholder="https://example.com/image.jpg"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="game">Игра</label>
                            <input
                                type="text"
                                id="game"
                                name="game"
                                value={formData.game}
                                onChange={handleInputChange}
                                placeholder="Название игры"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="stages">Этапы</label>
                            <input
                                type="text"
                                id="stages"
                                name="stages"
                                value={formData.stages}
                                onChange={handleInputChange}
                                placeholder="Этапы игры"
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="organizer">Организатор</label>
                            <input
                                type="text"
                                id="organizer"
                                name="organizer"
                                value={formData.organizer}
                                onChange={handleInputChange}
                                placeholder="Имя организатора"
                            />
                        </div>

                        {showMentionResults && (
                            <div className="mention-results">
                                {mentionResults.map(user => (
                                    <div
                                        key={user.username}
                                        className="mention-result"
                                        onClick={() => insertMention(user.username, user.avatarUrl)}
                                    >
                                        {user.avatarUrl && (
                                            <img src={user.avatarUrl} alt={user.username} />
                                        )}
                                        <span>@{user.username}</span>
                                    </div>
                                ))}
                            </div>
                        )}

                        <button 
                            type="submit" 
                            className="submit-button"
                        >
                            {editingEvent ? 'Сохранить изменения' : 'Создать событие'}
                        </button>
                    </form>
                </div>
            )}

            <div className="events-grid">
                {events.map(event => (
                    <div key={event.id} className="event-card">
                        {event.imageUrl && (
                            <div className="event-image-container">
                                <img 
                                    src={event.imageUrl} 
                                    alt={event.title} 
                                    className="event-image"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = '/placeholder-image.jpg';
                                    }}
                                />
                            </div>
                        )}
                        <div className="event-content">
                            <h3 className="event-title">{event.title}</h3>
                            <div className="event-description">
                                {parseMentions(event.description)}
                            </div>
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
                            <div className="event-actions">
                                <button 
                                    className="edit-button"
                                    onClick={() => handleEdit(event)}
                                >
                                    Редактировать
                                </button>
                                <button 
                                    className="delete-button"
                                    onClick={() => handleDelete(event.id)}
                                >
                                    Удалить
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default EventsPage;