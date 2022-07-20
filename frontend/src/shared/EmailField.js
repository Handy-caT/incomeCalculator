import React from 'react';

class EmailField extends React.Component {
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
            <div className="row mb-2 justify-content-center ">
                <div className="col-sm-2 col-form-label">
                    <label className={'form-label'} htmlFor="password">{this.props.label}</label>
                </div>
                <div className={'col-sm-5'}>
                    <input type="email" className={'form-control'} placeholder={this.props.placeholder}
                           value={value} onChange={this.handleChange} />
                </div>
            </div>
        );
    }
}

export default EmailField;