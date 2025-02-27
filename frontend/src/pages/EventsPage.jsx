import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function EventsPage() {
    const [events, setEvents] = useState([]);
    const [error, setError] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }

        axios.get('/api/events', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setEvents(response.data.data))
            .catch(err => {
                setError('Ошибка загрузки: ' + (err.response?.data?.message || 'Попробуйте снова'));
                if (err.response?.status === 401) navigate('/login');
            });
    }, [token, navigate]);

    return (
        <div>
            <h1>События</h1>
            {error && <p className="error">{error}</p>}
            <ul>
                {events.map(event => (
                    <li key={event.id}>
                        {event.title} - {event.location} ({new Date(event.date).toLocaleString()})
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default EventsPage;