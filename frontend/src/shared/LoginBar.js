import React from 'react';
import {UserContext} from "../context/user-context";
import {useNavigate} from "react-router";

function SignOut() {

    const userContext = React.useContext(UserContext);
    const nav = useNavigate();

     function signOut() {
        let user = userContext.user;
        user.deleteCookie();
        user.deleteSession();

        console.log(user);
        console.log(userContext.user);

        nav("/");
        window.location.reload();
    }

    return(
        <li><a className="dropdown-item" href="#" onClick={signOut} >Sign Out</a></li>
    );

}

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

    const userContext = React.useContext(UserContext);
    console.log(userContext.user);

    return (
        <ul className={'navbar-nav d-flex align-items-baseline'}>
            <li className="nav-item dropdown mx-2 fw-bold active">
                <a className="nav-link dropdown-toggle active" href="#" id="navbarDropdownMenuLink" role="button"
                   data-bs-toggle="dropdown" aria-expanded="false">
                    {userContext.user.name}
                </a>
                <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuLink">
                    <li><a className="dropdown-item" href="#">Profile</a></li>
                    <li><a className="dropdown-item" href="#">Settings</a></li>
                    <li><hr className="dropdown-divider" /></li>
                    <SignOut />
                </ul>
            </li>
        </ul>
    )
}


class LoginBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return(
          <UserContext.Consumer>
                {(userContext) => {
                    console.log(userContext)
                    if(userContext.login) {
                        return <LoginBarUser />
                    } else {
                        return <LoginBarNoUser />
                    }
                }}
          </UserContext.Consumer>
        );
    }
}

export default LoginBar;