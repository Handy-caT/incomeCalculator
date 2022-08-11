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
        let valid = '';
        if(this.props.valid !== undefined) {
            this.props.valid ? valid = 'is-valid' : valid = 'is-invalid';
        }
        return(
        <div className="row mb-2 justify-content-center ">
            <div className="col-sm-2 col-form-label">
                <label className={'form-label'} htmlFor="password">{this.props.label}</label>
            </div>
            <div className={'col-sm-5'}>
                <input type="password" className={'form-control ' + valid} placeholder={this.props.placeholder}
                       value={this.props.value} onChange={this.handleChange} />
                <div className="invalid-feedback">
                    {this.props.errorMessage}
                </div>
            </div>
        </div>
        );
    }
}

export default PasswordField;