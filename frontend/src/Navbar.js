import React from 'react';
import LoginBar from "./LoginBar";

class Navbar extends React.Component {
    render() {
        return (
        <header className="p-2 bg-dark text-light">
            <div className="container">
                <nav className="navbar navbar-expand-lg navbar-dark d-flex justify-content-between">
                    <ul className="navbar-nav d-flex align-items-baseline">
                        <li className="nav-item">
                            <a className="navbar-brand fw-bolder fs-4 mx-4" href="/">Income Calculator</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2" href="/">Home</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2" href="/about">About</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2" href="#">Help</a>
                        </li>
                    </ul>
                    <LoginBar />
                </nav>
            </div>
        </header>
        );
    }
}

export default Navbar;