import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './UserProfile.css';

const UserProfile = () => {
    const { username } = useParams();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`/api/users/${username}/profile`);
                setProfile(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to load profile');
                setLoading(false);
            }
        };

        fetchProfile();
    }, [username]);

    if (loading) return <div className="loading">Loading profile...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!profile) return <div className="error">Profile not found</div>;

    return (
        <div className="profile-container">
            <div className="profile-header">
                <div className="profile-avatar">
                    <img src={profile.avatarUrl || '/default-avatar.png'} alt={profile.username} />
                </div>
                <div className="profile-info">
                    <h1>{profile.nickname || profile.username}</h1>
                    <p className="role">{profile.role}</p>
                </div>
            </div>

            <div className="profile-content">
                {profile.bio && (
                    <div className="profile-section">
                        <h2>About</h2>
                        <p className="bio">{profile.bio}</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default UserProfile; 