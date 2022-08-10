import React from "react";
import './App.css';
import { Route, Routes} from 'react-router-dom';
import About from "./pages/About";
import Home from "./pages/Home";
import CurrencyRatios from "./pages/CurrencyRatios";
import SignUp from "./pages/SignUp";
import LogIn from "./pages/LogIn";
import Navbar from "./shared/Navbar";

class App extends React.Component {

    constructor(props) {
        super(props);

        this.handleUserChange = this.handleUserChange.bind(this);

        this.state = {
            user: null
        }
    }

    handleUserChange(user) {
        this.setState({user: user});
    }

    render() {
        return (
            <div className="App">
                <Navbar user={this.state.user} onUserChange={this.handleUserChange} />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/ratios" element={<CurrencyRatios />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/login" element={<LogIn user={this.state.user} onUserChange={this.handleUserChange} />} />
                </Routes>
            </div>
        );
    }
}

export default App;
