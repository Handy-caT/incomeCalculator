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

        this.state = {
            user: null
        }
    }

    render() {
        return (
            <div className="App">
                <Navbar />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/ratios" element={<CurrencyRatios />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/login" element={<LogIn />} />
                </Routes>
            </div>
        );
    }
}

export default App;
