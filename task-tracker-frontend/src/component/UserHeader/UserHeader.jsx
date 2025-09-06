import React, { useState } from 'react';
import BackupButton from "../BackupButton/BackupButton";
import ImportButton from '../ImportButton/ImportButton';
import './Header.css';
import {useAuth} from "../../provider/AuthProvider.jsx";

function UserHeader() {
    const {isAuthenticated, currentUser} = useAuth();

    return (
        <header className='app-header'>
            <div className='header-container'>
                <h1 className='app-name'>Todo-List</h1>
                <ImportButton/>
                <BackupButton/>
                <div className='user-info'>
                    {isAuthenticated ? <div>currentUser.name()</div> : <div></div>}
                </div>
            </div>
        </header>
    );
}

export default UserHeader; 