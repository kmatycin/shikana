import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './PilotProfile.css';

const PilotProfile = () => {
    const { nickname } = useParams();
    const [pilot, setPilot] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPilot = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`/api/pilots/${nickname}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setPilot(response.data);
                setError(null);
            } catch (err) {
                setError('Ошибка загрузки информации о пилоте');
                console.error('Error fetching pilot:', err);
                if (err.response?.status === 401) navigate('/login');
            } finally {
                setLoading(false);
            }
        };

        fetchPilot();
    }, [nickname, token, navigate]);

    const parseJsonField = (jsonString) => {
        try {
            const unescapedString = jsonString.replace(/&quot;/g, '"');
            return JSON.parse(unescapedString);
        } catch (e) {
            console.error('Error parsing JSON:', e);
            return null;
        }
    };

    const formatTitles = (titles) => {
        if (!titles) return null;
        const parsedTitles = parseJsonField(titles);
        if (!parsedTitles || !parsedTitles.title || !parsedTitles.year) return null;

        return (
            <div className="title-item">
                <span className="title-name">{parsedTitles.title}</span>
                <span className="title-year">{parsedTitles.year}</span>
            </div>
        );
    };

    const formatCars = (cars) => {
        if (!cars) return null;
        const parsedCars = parseJsonField(cars);
        if (!parsedCars || !parsedCars.model || !parsedCars.year) return null;

        return (
            <div className="car-item">
                <span className="car-model">{parsedCars.model}</span>
                <span className="car-year">{parsedCars.year}</span>
            </div>
        );
    };

    const formatRaceHistory = (history) => {
        if (!history) return null;
        const parsedHistory = parseJsonField(history);
        if (!parsedHistory || !Array.isArray(parsedHistory)) return null;
        
        return parsedHistory.map((race, index) => (
            <div key={index} className="race-item">
                <div className="race-header">
                    <span className="race-date">{new Date(race.date).toLocaleDateString()}</span>
                    <span className="race-position">P{race.position}</span>
                </div>
                <div className="race-name">{race.name}</div>
                <div className="race-points">
                    <span className="points-label">Очки лицензии:</span>
                    <span className="points-value">{race.licensePoints}</span>
                </div>
            </div>
        ));
    };

    if (loading) return <div className="loading">Загрузка информации о пилоте...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!pilot) return <div className="error-message">Пилот не найден</div>;

    return (
        <div className="pilots-container">
            <div className="pilot-card">
                <div className="pilot-header">
                    {pilot.photoUrl && (
                        <div className="pilot-image-container">
                            <img 
                                src={pilot.photoUrl} 
                                alt={pilot.nickname} 
                                className="pilot-image"
                                onError={(e) => {
                                    e.target.onerror = null;
                                    e.target.src = '/placeholder-pilot.jpg';
                                }}
                            />
                        </div>
                    )}
                    <div className="pilot-header-info">
                        <h3 className="pilot-name">{pilot.nickname}</h3>
                        <div className="pilot-number">{pilot.number}</div>
                    </div>
                </div>
                <div className="pilot-content">
                    <div className="pilot-details">
                        <div className="detail-row">
                            <span className="detail-label">Имя:</span>
                            <span className="detail-value">{pilot.realName}</span>
                        </div>
                        <div className="detail-row">
                            <span className="detail-label">Команда:</span>
                            <span className="detail-value">{pilot.team}</span>
                        </div>
                        <div className="detail-row">
                            <span className="detail-label">Место:</span>
                            <span className="detail-value">{pilot.city}, {pilot.country}</span>
                        </div>
                        {pilot.age && (
                            <div className="detail-row">
                                <span className="detail-label">Возраст:</span>
                                <span className="detail-value">{pilot.age}</span>
                            </div>
                        )}
                        <div className="detail-row">
                            <span className="detail-label">Дисциплина:</span>
                            <span className="detail-value">{pilot.discipline}</span>
                        </div>
                        <div className="detail-row">
                            <span className="detail-label">Лицензия:</span>
                            <span className="detail-value license-badge">{pilot.licenseLevel}</span>
                        </div>
                        <div className="detail-row">
                            <span className="detail-label">Победы:</span>
                            <span className="detail-value">
                                <span className="wins-season">{pilot.seasonWins}</span>
                                <span className="wins-total">/ {pilot.totalWins}</span>
                            </span>
                        </div>
                    </div>

                    {pilot.titles && (
                        <div className="pilot-titles">
                            <h4 className="section-title">Титулы</h4>
                            <div className="titles-list">
                                {formatTitles(pilot.titles)}
                            </div>
                        </div>
                    )}

                    {pilot.cars && (
                        <div className="pilot-cars">
                            <h4 className="section-title">Машины</h4>
                            <div className="cars-list">
                                {formatCars(pilot.cars)}
                            </div>
                        </div>
                    )}

                    {pilot.raceHistory && (
                        <div className="pilot-history">
                            <h4 className="section-title">История участий</h4>
                            <div className="race-history-list">
                                {formatRaceHistory(pilot.raceHistory)}
                            </div>
                        </div>
                    )}

                    <div className="pilot-status">
                        <span className={`status-badge ${pilot.status.toLowerCase()}`}>
                            {pilot.status}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PilotProfile; 