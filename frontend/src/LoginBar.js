import React from 'react';

class LoginBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const isLoggedIn = this.props.isLoggedIn;

        return (
        <ul className={'navbar-nav d-flex align-items-baseline'}>

        </ul>
        );
    }
}