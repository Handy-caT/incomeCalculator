import React from 'react';

class LoginBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
        <ul className={'navbar-nav d-flex align-items-baseline'}>
            <li className={'nav-item'}>
                <a className={'nav-link mx-2'} href="/">Sign Up</a>
            </li>
            <li className={'nav-item'}>
                <a className={'nav-link mx-2'} href="/">Log In</a>
            </li>
        </ul>
        );
    }
}

export default LoginBar;