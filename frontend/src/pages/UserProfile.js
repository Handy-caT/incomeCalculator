import React from "react";
import {useParams} from "react-router";
import UsernameField from "../shared/UsernameField";
import NameField from "../shared/NameField";
import EmailField from "../shared/EmailField";
import PasswordField from "../shared/PasswordField";

function User() {
    const params = useParams();
    return <div>User {params.login}</div>;
}

class UserProfile extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-3 ">
                        <h1 className={'mb-3 center'}>User Profile</h1>
                            <form>

                                <div className={'row justify-content-center'}>
                                    <div className="col-md-11">
                                        <div className="row mb-2 justify-content-center">
                                            <NameField
                                                value={'Mark'}
                                                label={'Name'}
                                                placeholder={'Name'} />
                                            <NameField
                                                value={'Mark'}
                                                label={'Name'}
                                                placeholder={'Name'} />
                                        </div>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        < UsernameField
                                            value={'mArk1'}
                                            label={'Username'}
                                            placeholder={'Username'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        < EmailField
                                            value={'mArk1'}
                                            label={'Email'}
                                            placeholder={'Email'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <PasswordField
                                            label={'Password confirmation'}
                                            placeholder={'Pa$$w0rd'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <div className="row mb-2 justify-content-center">
                                            <input type="button" className={'btn btn-success col-md-5'}
                                                      value="Confirm changes" />
                                            <div className="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>

                            </form>

                            <form className={'mt-3'}>
                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <div className="row mb-2 justify-content-center">
                                            <div className={'col-md-5'}>
                                                <h2>Password change</h2>
                                            </div>
                                            <div className="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <PasswordField
                                            label={'Old password'}
                                            placeholder={'Pa$$w0rd'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <PasswordField
                                            label={'New password'}
                                            placeholder={'-_Pa$swOr6_-'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <PasswordField
                                            label={'New password confirmation'}
                                            placeholder={'-_Pa$swOr6_-'}/>
                                    </div>
                                </div>

                                <div className="row mb-2 justify-content-center">
                                    <div className={'col-md-11'}>
                                        <div className="row mb-2 justify-content-center">
                                            <input type="button" className={'btn btn-success col-md-5'}
                                                   value="Change password" />
                                            <div className="col-md-3"></div>
                                        </div>
                                    </div>
                                </div>

                            </form>

                    </div>
                </div>
            </div>
        );
    }
}

export default UserProfile;