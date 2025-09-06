import React, {useEffect} from 'react';
import UserHeader from './component/UserHeader/UserHeader';
import TodoList from './component/TodoList/TodoList';
import WeekScrollButton from './component/WeekScrollButton/WeekScrollButton';
import './App.css';
import {TasksProvider} from './provider/TasksProvider';
import {ModalProvider} from './provider/ModalProvider';
import {AuthProvider, useAuth} from "./provider/AuthProvider.jsx";
import AuthPage from "./component/AuthPage/AuthPage.jsx";
import LoadingSpinner from "./component/LoadingSpinner/LoadingSpinner.jsx";

function App() {
    return (
        <AuthProvider>
            <AppContent/>
        </AuthProvider>
    )
}

function AppContent() {
    const {isAuthenticated, isLoading, fetchUserData} = useAuth();

    useEffect(() => {
        fetchUserData();
    }, [])

    return (
        <ModalProvider>
            <TasksProvider>
                <div className="app-layout">
                    {isLoading ? <LoadingSpinner/>
                        : isAuthenticated ? (
                                <>
                                    <UserHeader/>
                                    <WeekScrollButton/>
                                    <main>
                                        <TodoList/>
                                    </main>
                                </>
                            ) :
                            <AuthPage/>
                    }
                </div>
            </TasksProvider>
        </ModalProvider>
    );
}

export default App;