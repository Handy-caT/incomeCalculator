import React from "react";

class PasswordField extends React.Component {
    constructor(props) {
        super(props);

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        this.props.onValueChange(event.target.value);
    }

    render() {
        const value = this.props.value;
        return(
        <div className="row mb-3 justify-content-center ">
            <div className="col-sm-2 col-form-label">
                <label className={'form-label'} htmlFor="password">{this.props.label}</label>
            </div>
            <div className={'col-sm-5'}>
                <input type="password" className={'form-control'} placeholder={'Pa$$w0rd'}
                       value={value} onChange={this.handleChange} />
            </div>
        </div>
        );
    }
}

export default PasswordField;