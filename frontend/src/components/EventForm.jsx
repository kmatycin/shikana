import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './EventForm.css';

function EventForm() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        date: '',
        location: '',
        imageUrl: '',
        isExternal: false
    });
    
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const endpoint = formData.isExternal ? '/api/events/external' : '/api/events';
            await axios.post(endpoint, formData, {
                headers: { Authorization: `Bearer ${token}` }
            });
            navigate('/events');
        } catch (err) {
            setError('Ошибка при создании события: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="event-form-container">
            <h2>Создание нового события</h2>
            {error && <p className="error-message">{error}</p>}
            
            <form onSubmit={handleSubmit} className="event-form">
                <div className="form-group">
                    <label htmlFor="title">Название события *</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="description">Описание</label>
                    <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
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
                        onChange={handleChange}
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
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="imageUrl">URL изображения</label>
                    <input
                        type="url"
                        id="imageUrl"
                        name="imageUrl"
                        value={formData.imageUrl}
                        onChange={handleChange}
                        placeholder="https://example.com/image.jpg"
                    />
                </div>

                <div className="form-group checkbox">
                    <label>
                        <input
                            type="checkbox"
                            name="isExternal"
                            checked={formData.isExternal}
                            onChange={handleChange}
                        />
                        Внешнее событие
                    </label>
                </div>

                <button 
                    type="submit" 
                    className="submit-button"
                    disabled={loading}
                >
                    {loading ? 'Создание...' : 'Создать событие'}
                </button>
            </form>
        </div>
    );
}

export default EventForm; 