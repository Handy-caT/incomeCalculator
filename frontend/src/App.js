import React from "react";
import './App.css';
import { Route, Routes} from 'react-router-dom';
import About from "./pages/About";
import Home from "./pages/Home";
import CurrencyRatios from "./pages/CurrencyRatios";

class App extends React.Component {
    render() {
        return (
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/about" element={<About />} />
                    <Route path="/ratios" element={<CurrencyRatios />} />
                </Routes>
            </div>
        );
    }
}

export default App;
