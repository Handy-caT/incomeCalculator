import React from 'react';
import {useNavigate} from "react-router";
import {UserContext} from "../context/user-context";

function LoginBarNoUser() {
    return (
        <ul className={'navbar-nav d-flex align-items-baseline'}>
            <li className={'nav-item'}>
                <a className={'nav-link mx-2 fw-bold'} href="/signup">Sign Up</a>
            </li>
            <li className={'nav-item'}>
                <a className={'nav-link mx-2 fw-bold'} href="/login">Log In</a>
            </li>
        </ul>
    );
}

function LoginBarUser() {

    const nav = useNavigate();
    const userContext = React.useContext(UserContext);

    console.log(userContext.user);

    return (
        <ul className={'navbar-nav d-flex align-items-baseline'}>
            <li className={'nav-item'}>
                <a className={'navbar-brand mx-2 fw-bold'} href="/signup">{userContext.user.name}</a>
            </li>
            <li className={'nav-item'}>
                <a className={'nav-link mx-2 fw-bold'} href="/login">Log In</a>
            </li>
        </ul>
    )
}


class LoginBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const userName = sessionStorage.getItem('name');
        if (userName) {
            return <LoginBarUser />
        } else {
            return <LoginBarNoUser />
        }
    }
}

export default LoginBar;