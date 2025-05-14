import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Header from './components/Header';
import EventsPage from './pages/EventsPage';
import NewsPage from './pages/NewsPage';
import ProfilePage from './pages/ProfilePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import EventForm from './components/EventForm';
import PilotProfile from './pages/PilotProfile';
import PilotsPage from './pages/PilotsPage';
import './App.css';

function App() {
    return (
        <Router>
            <div className="app">
                <Header />
                <main className="main-content">
                    <Routes>
                        <Route path="/" element={<EventsPage />} />
                        <Route path="/news" element={<NewsPage />} />
                        <Route path="/profile" element={<ProfilePage />} />
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/events/create" element={<EventForm />} />
                        <Route path="/pilots" element={<PilotsPage />} />
                        <Route path="/pilots/:nickname" element={<PilotProfile />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

export default App;