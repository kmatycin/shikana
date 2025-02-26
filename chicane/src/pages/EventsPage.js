import React, { useEffect, useState } from 'react';
import axios from 'axios';

function EventsPage() {
    const [events, setEvents] = useState([]);
    const token = 'your-jwt-token-here'; // Замените на реальный токен

    useEffect(() => {
        axios.get('/api/events', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => setEvents(response.data.data))
            .catch(error => console.error('Ошибка загрузки событий:', error));
    }, []);

    return (
        <div>
            <h1>События</h1>
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