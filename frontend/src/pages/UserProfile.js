import React from "react";
import {useParams} from "react-router";
import UsernameField from "../shared/UsernameField";
import NameField from "../shared/NameField";

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
                        <div className="flex-row">
                            <form>

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
                                < UsernameField
                                    value={'mArk1'}
                                    label={'Username'}
                                    placeholder={'Username'}/>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default UserProfile;