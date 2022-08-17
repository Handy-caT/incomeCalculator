import React from 'react';
import UsernameField from "../shared/UsernameField";
import PasswordField from "../shared/PasswordField";
import UserModel from "../classes/UserModel";
import {useNavigate} from "react-router";
import {UserContext} from "../context/user-context";
import {authenticate, registerCookie} from "../classes/ApiConnection";


function LogInButton(props) {

    const nav = useNavigate();
    const userContext = React.useContext(UserContext);

    async function logIn() {
        let authUser = new UserModel(props.username, props.cookieAgreement);
        try {
            const response = await authenticate(props.username, props.password);
            console.log(response);
            props.handleError(false);
            authUser.setToken(response.token);

            if(props.cookieAgreement) {
                const response = await registerCookie(props.username, props.password);
                console.log(response);
                authUser.setToken(response.token);
                authUser.setId(response.userId);
            }

            userContext.setUser(authUser);
            userContext.setLogin(true);
            nav("/");
        } catch (error) {
            console.log(error);
            props.handleError(true);
        }

        console.log(authUser);
        console.log(userContext.user);
    }

    return (
        <div className="row mt-3 mb-1 justify-content-center ">
            <input type="button" className={'btn btn-primary col-5 mb-2'} value="Log In"
                   onClick={logIn}/>
        </div>
    );
}

class LogIn extends React.Component {

    constructor(props) {
        super(props);

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleCookieAgreement = this.handleCookieAgreement.bind(this);
        this.handleError = this.handleError.bind(this);

        this.state = {
            username: '',
            password: '',
            cookieAgreement: false,
            error: false,
        }
    }

    handleUsernameChange(username) {
        this.setState({username: username});
    }
    handlePasswordChange(password) {
        this.setState({password: password});
    }
    handleCookieAgreement() {
        const newCookieAgreement = !this.state.cookieAgreement;
        this.setState({cookieAgreement: newCookieAgreement});
    }
    handleError(error) {
        this.setState({error: error});
    }




    render() {
        let alert;
        if (this.state.error) {
            alert = (<div className="alert alert-danger" role="alert">
                Wrong username or password. Try again!
            </div>)
        } else {
            alert = null;
        }

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-3">
                        {alert}
                        <h1 className={'mb-3 center'}>Log In</h1>
                        <div className="row mb-1 justify-content-center">

                        <UsernameField
                            onValueChange={this.handleUsernameChange}
                            value={this.state.email}
                            label={'Username'}
                            placeholder={'Username'}/>
                        <PasswordField
                            onValueChange={this.handlePasswordChange}
                            value={this.state.password}
                            label={'Password'}
                            placeholder={'Password'}/>


                            <div className="col-sm-2 col-form-label"></div>
                            <div className="col-sm-5">
                                <input type="checkbox" className="form-check-input" id="exampleCheck1"
                                       onClick={this.handleCookieAgreement}/>
                                <label className="text-secondary mx-1"> Remember me on this computer</label>
                            </div>
                        </div>

                        <LogInButton username={this.state.username} password={this.state.password}
                                        cookieAgreement={this.state.cookieAgreement}
                                        handleError={this.handleError} onUserChange={this.props.onUserChange}/>

                        <div className="row mb-3 justify-content-center">
                            <p className="col-sm-2 "><strong>New to Sample App?</strong></p>
                            <div className="col-sm-5">
                                <a className={'btn btn-outline-info btn-sm'} href="/signup">Sign up now!</a>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        );

    }
}

export default LogIn;

