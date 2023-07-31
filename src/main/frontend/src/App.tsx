
import { Navigate, Route, Routes } from 'react-router-dom';

import Layout from './components/Layout/Layout';
import AuthPage from './pages/AuthPage';
import CreateAccountPage from './pages/CreateAccountPage';
import HomePage from './pages/HomePage';
import ProfilePage from './pages/ProfilePage';
import AuthContext from './store/auth-context';
import ChatRoomList from './components/Chat/ChatRoomList';
import ChatPage from './components/Chat/ChatPage';
import {useContext} from "react";
import AddItemPage from './pages/AddItemPage';
import ItemPage from 'pages/ItemPage';

function App() {

    const authCtx = useContext(AuthContext);
    console.log("MAIN");

    return (
        <Layout>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/signup/" element={authCtx.isLoggedIn ? <Navigate to='/' /> : <CreateAccountPage />} />
                <Route path="/login/*"
                    element={authCtx.isLoggedIn ? <Navigate to='/' /> : <AuthPage />}
                />
                <Route path="/profile/" element={!authCtx.isLoggedIn ? <Navigate to='/' /> : <ProfilePage />} />
                <Route path="/chat/all/rooms" element={!authCtx.isLoggedIn ? <Navigate to='/' /> : <ChatRoomList />} />
                <Route path="/item-add-view" element={!authCtx.isLoggedIn ? <Navigate to='/' /> :<AddItemPage/>} />
                <Route path="/item-view/:itemId" element={<ItemPage id={''} />} />

                <Route path="/chat/room/:roomId" element={<ChatPage id={''} />} />
                
            </Routes>

        </Layout>
    );
}

export default App;