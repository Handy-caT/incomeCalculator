import React from 'react';
import UsernameField from "../shared/UsernameField";
import PasswordField from "../shared/PasswordField";
import EmailField from "../shared/EmailField";

class SignUp extends React.Component {
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
                        <h1 className={'mb-3 center'}>Sign Up</h1>
                        <div>
                            <form>
                                <UsernameField
                                    onValueChange={this.handleNameChange}
                                    value={this.state.name}
                                    label={'Username'}
                                    placeholder={'Username'}/>
                                <EmailField
                                    onValueChange={this.handleEmailChange}
                                    value={this.state.email}
                                    label={'Email'}
                                    placeholder={'name@example.com'}/>
                                <PasswordField
                                    onValueChange={this.handlePasswordChange}
                                    value={this.state.password}
                                    label={'Password'}
                                    placeholder={'Pa$$w0rd'}/>
                                <PasswordField
                                    onValueChange={this.handlePasswordChange}
                                    value={this.state.password}
                                    label={'Password confirmation'}
                                    placeholder={'Pa$$w0rd'}/>

                                <div className="row my-3 justify-content-center ">
                                 <input type="button" className={'btn btn-primary col-5'} value="Create me account" />
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default SignUp;