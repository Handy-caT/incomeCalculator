import React from 'react';
import LoginBar from "./LoginBar";

class Navbar extends React.Component {
    render() {
        return (
        <header className="p-2 background">
            <div className="container">
                <nav className="navbar navbar-expand navbar-light d-flex justify-content-between">
                    <ul className="navbar-nav d-flex align-items-baseline">
                        <li className="nav-item">
                            <a className="navbar-brand fs-4 fw-bolder" href="/">Income Calculator</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2 fw-bold" href="/">Home</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2 fw-bold" href="/about">About</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link mx-2 fw-bold" href="/ratios">Ratios</a>
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