import React from 'react';
import ReactDOM from 'react-dom/client';

import {BrowserRouter} from 'react-router-dom';

import App from './App';
import {AuthContextProvider} from './store/auth-context';
import 'bootstrap/dist/css/bootstrap.css';
import './index.css'

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

root.render(
    <AuthContextProvider>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </AuthContextProvider>
);