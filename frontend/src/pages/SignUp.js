import React from 'react';

class SignUp extends React.Component {
    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-12 mt-3 ">
                        <h1 className={'mb-3 center '}>Sign Up</h1>
                        <div className="flex-row">
                            <form>
                                <div className="row mb-3 justify-content-center ">
                                    <div className="col-sm-2 col-form-label">
                                        <label className={'form-label'} htmlFor="firstName">First Name</label>
                                    </div>
                                    <div className={'col-sm-5'}>
                                        <input type="text" className={'form-control'} placeholder={'Name'} />
                                    </div>
                                </div>

                                <div className="row mb-3 justify-content-center ">
                                    <div className="col-sm-2 col-form-label">
                                        <label className={'form-label'} htmlFor="email">Email</label>
                                    </div>
                                    <div className={'col-sm-5'}>
                                        <input type="email" className={'form-control'} placeholder={'name@example.com'} />
                                    </div>
                                </div>

                                <div className="row mb-3 justify-content-center ">
                                    <div className="col-sm-2 col-form-label">
                                        <label className={'form-label'} htmlFor="password">Password</label>
                                    </div>
                                    <div className={'col-sm-5'}>
                                        <input type="password" className={'form-control'} placeholder={'Pa$$w0rd'} />
                                    </div>
                                </div>

                                <div className="row mb-3 justify-content-center ">
                                    <div className="col-sm-2 col-form-label">
                                        <label className={'form-label'} htmlFor="passwordConfirmation">Password Confirmation</label>
                                    </div>
                                    <div className={'col-sm-5'}>
                                        <input type="password" className={'form-control'} placeholder={'Pa$$w0rd'} />
                                    </div>
                                </div>

                                <div className="row mb-3 justify-content-center ">
                                 <input type="button" className={'btn btn-primary col-5'} value="Sign Up" />
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