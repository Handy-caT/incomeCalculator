import React from "react";

class NameField extends React.Component {
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
                <div className="col-sm-4 mb-2 ">
                    <label htmlFor="validationCustom01" className="form-label">{this.props.label}</label>
                    <input type="text" className={"form-control " + valid } id="validationCustom01" value={this.props.value}
                           required />
                        <div className="valid-feedback">
                            {this.props.successMessage}
                        </div>
                </div>
        );
    }
}

export default NameField;