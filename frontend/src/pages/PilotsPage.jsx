import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './PilotsPage.css';

function PilotsPage() {
    const [pilots, setPilots] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [editingPilot, setEditingPilot] = useState(null);
    const [formData, setFormData] = useState({
        nickname: '',
        realName: '',
        team: '',
        country: '',
        city: '',
        number: '',
        age: '',
        discipline: '',
        licenseLevel: '',
        titles: '',
        seasonWins: 0,
        totalWins: 0,
        cars: '',
        photoUrl: '',
        isActive: true,
        status: 'Active'
    });
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            navigate('/login');
            return;
        }
        fetchPilots();
    }, [token, navigate]);

    const fetchPilots = async () => {
        setLoading(true);
        try {
            const response = await axios.get('/api/pilots', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setPilots(response.data);
        } catch (err) {
            setError('Ошибка загрузки пилотов: ' + (err.response?.data?.message || 'Попробуйте снова'));
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
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (editingPilot) {
                await axios.put(`/api/pilots/${editingPilot.nickname}`, formData, {
                    headers: { Authorization: `Bearer ${token}` }
                });
            } else {
                await axios.post('/api/pilots', formData, {
                    headers: { Authorization: `Bearer ${token}` }
                });
            }
            fetchPilots();
            resetForm();
        } catch (err) {
            setError('Ошибка при сохранении пилота: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const handleEdit = (pilot) => {
        setEditingPilot(pilot);
        setFormData({
            nickname: pilot.nickname || '',
            realName: pilot.realName || '',
            team: pilot.team || '',
            country: pilot.country || '',
            city: pilot.city || '',
            number: pilot.number || '',
            age: pilot.age || '',
            discipline: pilot.discipline || '',
            licenseLevel: pilot.licenseLevel || '',
            titles: pilot.titles || '',
            seasonWins: pilot.seasonWins || 0,
            totalWins: pilot.totalWins || 0,
            cars: pilot.cars || '',
            photoUrl: pilot.photoUrl || '',
            isActive: pilot.isActive ?? true,
            status: pilot.status || 'Active'
        });
        setShowForm(true);
    };

    const handleDelete = async (nickname) => {
        if (!window.confirm('Вы уверены, что хотите удалить этого пилота?')) {
            return;
        }

        try {
            await axios.delete(`/api/pilots/${nickname}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            await fetchPilots();
        } catch (err) {
            setError('Ошибка при удалении пилота: ' + (err.response?.data?.message || 'Попробуйте снова'));
            if (err.response?.status === 401) navigate('/login');
        }
    };

    const resetForm = () => {
        setFormData({
            nickname: '',
            realName: '',
            team: '',
            country: '',
            city: '',
            number: '',
            age: '',
            discipline: '',
            licenseLevel: '',
            titles: '',
            seasonWins: 0,
            totalWins: 0,
            cars: '',
            photoUrl: '',
            isActive: true,
            status: 'Active'
        });
        setEditingPilot(null);
        setShowForm(false);
    };

    const parseJsonField = (jsonString) => {
        try {
            return JSON.parse(jsonString);
        } catch (e) {
            return null;
        }
    };

    const formatTitles = (titles) => {
        if (!titles) return null;
        const parsedTitles = parseJsonField(titles);
        if (!parsedTitles) return null;

        if (Array.isArray(parsedTitles)) {
            return parsedTitles.map((title, index) => (
                <div key={index} className="title-item">
                    <span className="title-year">{title.year}</span>
                    <span className="title-name">{title.title}</span>
                </div>
            ));
        } else {
            return (
                <div className="title-item">
                    <span className="title-year">{parsedTitles.year}</span>
                    <span className="title-name">{parsedTitles.title}</span>
                </div>
            );
        }
    };

    const formatCars = (cars) => {
        if (!cars) return null;
        const parsedCars = parseJsonField(cars);
        if (!parsedCars) return null;

        if (Array.isArray(parsedCars)) {
            return parsedCars.map((car, index) => (
                <div key={index} className="car-item">
                    <span className="car-model">{car.model}</span>
                    <span className="car-year">{car.year}</span>
                </div>
            ));
        } else {
            return (
                <div className="car-item">
                    <span className="car-model">{parsedCars.model}</span>
                    <span className="car-year">{parsedCars.year}</span>
                </div>
            );
        }
    };

    if (loading) return <div className="loading">Загрузка пилотов...</div>;

    return (
        <div className="pilots-container">
            <div className="pilots-header">
                <h1 className="pilots-title">Управление пилотами</h1>
                <button 
                    className="create-pilot-button"
                    onClick={() => {
                        if (showForm) {
                            resetForm();
                        } else {
                            setShowForm(true);
                        }
                    }}
                >
                    {showForm ? 'Отмена' : 'Добавить пилота'}
                </button>
            </div>
            {error && <p className="error-message">{error}</p>}

            {showForm && (
                <div className="pilot-form-container">
                    <h2>{editingPilot ? 'Редактирование пилота' : 'Добавление нового пилота'}</h2>
                    
                    <form onSubmit={handleSubmit} className="pilot-form">
                        <div className="form-section">
                            <h3>Основная информация</h3>
                            <div className="form-group">
                                <label htmlFor="nickname">Никнейм *</label>
                                <input
                                    type="text"
                                    id="nickname"
                                    name="nickname"
                                    value={formData.nickname}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="realName">Настоящие имя и фамилия *</label>
                                <input
                                    type="text"
                                    id="realName"
                                    name="realName"
                                    value={formData.realName}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="team">Команда *</label>
                                <input
                                    type="text"
                                    id="team"
                                    name="team"
                                    value={formData.team}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="country">Страна *</label>
                                <input
                                    type="text"
                                    id="country"
                                    name="country"
                                    value={formData.country}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="city">Город *</label>
                                <input
                                    type="text"
                                    id="city"
                                    name="city"
                                    value={formData.city}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="number">Номер *</label>
                                <input
                                    type="text"
                                    id="number"
                                    name="number"
                                    value={formData.number}
                                    onChange={handleInputChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="age">Возраст</label>
                                <input
                                    type="number"
                                    id="age"
                                    name="age"
                                    value={formData.age}
                                    onChange={handleInputChange}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="photoUrl">URL фотографии</label>
                                <input
                                    type="url"
                                    id="photoUrl"
                                    name="photoUrl"
                                    value={formData.photoUrl}
                                    onChange={handleInputChange}
                                    placeholder="https://example.com/photo.jpg"
                                />
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Спортивная информация</h3>
                            <div className="form-group">
                                <label htmlFor="discipline">Дисциплина *</label>
                                <select
                                    id="discipline"
                                    name="discipline"
                                    value={formData.discipline}
                                    onChange={handleInputChange}
                                    required
                                >
                                    <option value="">Выберите дисциплину</option>
                                    <option value="Formula 1">Формула 1</option>
                                    <option value="Formula 2">Формула 2</option>
                                    <option value="Formula 3">Формула 3</option>
                                    <option value="GT3">GT3</option>
                                    <option value="GT4">GT4</option>
                                    <option value="DTM">DTM</option>
                                    <option value="WTCR">WTCR</option>
                                    <option value="WEC">WEC</option>
                                    <option value="Formula E">Формула E</option>
                                    <option value="IndyCar">IndyCar</option>
                                    <option value="NASCAR">NASCAR</option>
                                    <option value="WRC">WRC</option>
                                    <option value="Other">Другое</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="licenseLevel">Уровень лицензии *</label>
                                <select
                                    id="licenseLevel"
                                    name="licenseLevel"
                                    value={formData.licenseLevel}
                                    onChange={handleInputChange}
                                    required
                                >
                                    <option value="">Выберите уровень</option>
                                    <option value="A">A</option>
                                    <option value="B">B</option>
                                    <option value="C">C</option>
                                    <option value="D">D</option>
                                    <option value="E">E</option>
                                    <option value="F">F</option>
                                    <option value="G">G</option>
                                    <option value="H">H</option>
                                    <option value="I">I</option>
                                    <option value="J">J</option>
                                </select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="titles">Титулы</label>
                                <textarea
                                    id="titles"
                                    name="titles"
                                    value={formData.titles}
                                    onChange={handleInputChange}
                                    rows="3"
                                    placeholder='{"year": "2023", "title": "Чемпион Формулы 1"}'
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="seasonWins">Победы в сезоне *</label>
                                <input
                                    type="number"
                                    id="seasonWins"
                                    name="seasonWins"
                                    value={formData.seasonWins}
                                    onChange={handleInputChange}
                                    min="0"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="totalWins">Всего побед *</label>
                                <input
                                    type="number"
                                    id="totalWins"
                                    name="totalWins"
                                    value={formData.totalWins}
                                    onChange={handleInputChange}
                                    min="0"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="cars">Машины *</label>
                                <textarea
                                    id="cars"
                                    name="cars"
                                    value={formData.cars}
                                    onChange={handleInputChange}
                                    rows="3"
                                    placeholder='{"model": "Mercedes-AMG F1 W14", "year": "2023"}'
                                    required
                                />
                            </div>
                        </div>

                        <div className="form-section">
                            <h3>Статус</h3>
                            <div className="form-group">
                                <label htmlFor="isActive">Активен</label>
                                <input
                                    type="checkbox"
                                    id="isActive"
                                    name="isActive"
                                    checked={formData.isActive}
                                    onChange={(e) => setFormData(prev => ({
                                        ...prev,
                                        isActive: e.target.checked
                                    }))}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="status">Статус</label>
                                <select
                                    id="status"
                                    name="status"
                                    value={formData.status}
                                    onChange={handleInputChange}
                                >
                                    <option value="Active">Активен</option>
                                    <option value="Inactive">Неактивен</option>
                                    <option value="Retired">На пенсии</option>
                                </select>
                            </div>
                        </div>

                        <button 
                            type="submit" 
                            className="submit-button"
                        >
                            {editingPilot ? 'Сохранить изменения' : 'Добавить пилота'}
                        </button>
                    </form>
                </div>
            )}

            <div className="pilots-grid">
                {Array.isArray(pilots) && pilots.map(pilot => (
                    <div key={pilot.id} className="pilot-card">
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

                            <div className="pilot-status">
                                <span className={`status-badge ${pilot.status.toLowerCase()}`}>
                                    {pilot.status}
                                </span>
                            </div>

                            <div className="pilot-actions">
                                <button 
                                    className="edit-button"
                                    onClick={() => handleEdit(pilot)}
                                >
                                    Редактировать
                                </button>
                                <button 
                                    className="delete-button"
                                    onClick={() => handleDelete(pilot.nickname)}
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

export default PilotsPage; 