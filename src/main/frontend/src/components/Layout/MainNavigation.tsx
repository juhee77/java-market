import React, {useContext, useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

import AuthContext from '../../store/auth-context';


const MainNavigation = () => {

    const authCtx = useContext(AuthContext);
    const [nickname, setNickname] = useState('');
    let isLogin = authCtx.isLoggedIn;
    let isGet = authCtx.isGetSuccess;

    const callback = (str: string, url: string) => {
        setNickname(str);
    }

    useEffect(() => {
        if (isLogin) {
            console.log('MainNavigation start -> ' + authCtx.userObj.username);
            authCtx.getUser();
        }
    }, [isLogin]);

    useEffect(() => {
        if (isGet) {
            console.log('MainNavigation get start -> ' + authCtx.userObj.nickname);
            callback(authCtx.userObj.nickname, authCtx.userObj.username);
        }
    }, [isGet]);


    const toggleLogoutHandler = () => {
        authCtx.logout();
    }


    return (
        <header>
            <nav className="navbar navbar-expand-sm bg-light">
                <div className="container-fluid">
                    <Link className="navbar-brand" to="/">
                        Home
                    </Link>
                    <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                        <ul className="navbar-nav">
                            {!isLogin && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/login">
                                            Login
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/signup">
                                            Sign-Up
                                        </Link>
                                    </li>
                                </>

                            )}
                            {isLogin && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/profile">
                                            {nickname}
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/item-add-view">
                                            아이템 생성
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/negotiation-view//approve">
                                            나에게 도착한 제안
                                        </Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/negotiation-view/suggest">
                                            내가한 제안
                                        </Link>
                                    </li>

                                    <li className="nav-item">
                                        <Link className="nav-link" to="/chat/all/rooms">
                                            연락온 채팅
                                        </Link>

                                    </li>
                                    <li className="nav-item">
                                        <button className="btn btn-primary" onClick={toggleLogoutHandler}>
                                            Logout
                                        </button>
                                    </li>
                                </>
                            )}
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
    );
};

export default MainNavigation;