import React from 'react';
import NameField from "../shared/NameField";
import PasswordField from "../shared/PasswordField";

class LogIn extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            password_confirmation: '',
            name: ''
        }

        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handlePasswordConfirmationChange = this.handlePasswordConfirmationChange.bind(this);
        this.handleNameChange = this.handleNameChange.bind(this);
    }

    handleEmailChange(event) {
        this.setState({email: event.target.value});
    }
    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }
    handlePasswordConfirmationChange(event) {
        this.setState({password_confirmation: event.target.value});
    }
    handleNameChange(event) {
        this.setState({name: event.target.value});
    }


    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-3">
                        <h1 className={'mb-3 center'}>Log In</h1>

                        <NameField
                            onValueChange={this.handleNameChange}
                            value={this.state.name}
                            label={'Name'}/>
                        <PasswordField
                            onValueChange={this.handlePasswordChange}
                            value={this.state.password}
                            label={'Password'}/>


                    </div>
                </div>
            </div>
        );
    }
}

export default LogIn;