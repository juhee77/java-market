import React, { useContext, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';

const Signup = () => {

    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const passwordInputRef = useRef<HTMLInputElement>(null);
    const repasswordInputRef = useRef<HTMLInputElement>(null);
    const nicknameInputRef = useRef<HTMLInputElement>(null);
    const usernameInputRef = useRef<HTMLInputElement>(null);

    const submitHandler = async (event: React.FormEvent) => {
        event.preventDefault();

        const enteredUsername = usernameInputRef.current!.value;
        const enteredPassword = passwordInputRef.current!.value;
        const enteredNickname = nicknameInputRef.current!.value;
        const enteredRePassword = repasswordInputRef.current!.value;

        authCtx.signup(enteredUsername, enteredPassword, enteredNickname, enteredRePassword);


        if (authCtx.isSuccess) {
            navigate("/login", { replace: true });
        }else{
            navigate("/signup", { replace: true });
        }
    }

    return (
        <section>
            <h1>Create Account</h1>
            <form onSubmit={submitHandler} className="form-horizontal">
                <div className="form-group">
                    <label htmlFor='username' className="col-sm-2 control-label">Your username</label>
                    <input type='username' id='username' className="form-control" required ref={usernameInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="password" className="col-sm-2 control-label">Your password</label>
                    <input type='password' id='password' className="form-control" required ref={passwordInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="repassword" className="col-sm-2 control-label">Your password</label>
                    <input type='repassword' id='repassword' className="form-control" required ref={repasswordInputRef} />
                </div>
                <div className="form-group">
                    <label htmlFor="nickname" className="col-sm-2 control-label">NickName</label>
                    <input type='nickname' id='nickname' className="form-control" required ref={nicknameInputRef} />
                </div>

                <div>
                    <button type='submit' className="btn btn-primary btn"> Submit</button>
                </div>
            </form>
        </section>
    );

};
export default Signup;