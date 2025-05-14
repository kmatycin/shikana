import React from 'react';
import { Link } from 'react-router-dom';
import './UserMention.css';

const UserMention = ({ username, avatarUrl }) => {
    return (
        <Link to={`/pilots/${username}`} className="user-mention">
            {avatarUrl && <img src={avatarUrl} alt={username} className="mention-avatar" />}
            <span className="mention-username">@{username}</span>
        </Link>
    );
};

export default UserMention; 