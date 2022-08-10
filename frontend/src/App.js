import React from "react";
import './App.css';
import { Route, Routes} from 'react-router-dom';
import About from "./pages/About";
import Home from "./pages/Home";
import CurrencyRatios from "./pages/CurrencyRatios";
import SignUp from "./pages/SignUp";
import LogIn from "./pages/LogIn";
import Navbar from "./shared/Navbar";
import {UserContext} from "./context/user-context";
import UserModel from "./classes/UserModel";

class App extends React.Component {

    constructor(props) {
        super(props);

        this.setUser = this.setUser.bind(this);

        let user = new UserModel();
        if(!user.loadCookie()) {
            user.loadSession();
        }
        this.state = {
            user: user
        }
    }

    setUser(user) {
        user.saveSession();
        user.cookieAgreement ? user.saveCookie() : user.deleteCookie();
        this.setState({user: user});
    }

    render() {
        return (
            <div className="App">
                <UserContext.Provider value={{user: this.state.user,
                                                setUser: this.setUser}}>
                    <Navbar />
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/about" element={<About />} />
                        <Route path="/ratios" element={<CurrencyRatios />} />
                        <Route path="/signup" element={<SignUp />} />
                        <Route path="/login" element={<LogIn />} />
                    </Routes>
                </UserContext.Provider>
            </div>
        );
    }
}

export default App;
