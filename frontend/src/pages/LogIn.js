import React from 'react';
import NameField from "../shared/NameField";
import PasswordField from "../shared/PasswordField";
import EmailField from "../shared/EmailField";
import axios from "axios";

class LogIn extends React.Component {

    constructor(props) {
        super(props);

        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);

        this.state = {
            email: '',
            password: '',
        }
    }

    handleEmailChange(email) {
        const passwordOld = this.state.password;
        this.setState({email: email, password: passwordOld});
    }
    handlePasswordChange(password) {
        const emailOld = this.state.email;
        this.setState({password: password, email: emailOld});
    }

    logIn(object) {

        console.log(object)

        axios.post('http://localhost:8080/auth', {
            login: 'zuka',
            password: 'test'
        })
            .then(function (response) {
                console.log(response);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-3">
                        <h1 className={'mb-3 center'}>Log In</h1>

                        <EmailField
                            onValueChange={this.handleEmailChange}
                            value={this.state.email}
                            label={'Email'}
                            placeholder={'name@example.com'}/>
                        <PasswordField
                            onValueChange={this.handlePasswordChange}
                            value={this.state.password}
                            label={'Password'}
                            placeholder={'Password'}/>

                        <div className="row mb-1 justify-content-center">
                            <div className="col-sm-2 col-form-label"></div>
                            <div className="col-sm-5">
                                <input type="checkbox" className="form-check-input" id="exampleCheck1" />
                                <label className="text-secondary mx-1"> Remember me on this computer</label>
                            </div>
                        </div>

                        <div className="row mt-3 mb-1 justify-content-center ">
                            <input type="button" className={'btn btn-primary col-5 mb-2'} value="Log In"
                                   onClick={this.logIn.bind(this.state)} />
                        </div>

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