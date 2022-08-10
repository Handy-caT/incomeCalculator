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
        this.setLogin = this.setLogin.bind(this);

        this.state = {
            user: {},
            login: false,
        }
    }

    componentDidMount() {
        let user = new UserModel();
        if(!user.loadCookie()) {
            this.setState({user: {}, login: false});
        } else {
            this.setState({user: user, login: true});
        }
    }

    setUser(user) {
        user.saveSession();
        user.cookieAgreement ? user.saveCookie() : user.deleteCookie();
        this.setState({user: user});
    }

    setLogin(login) {
        this.setState({login: login});
    }

    render() {
        return (
            <div className="App">
                <UserContext.Provider value={{user: this.state.user,
                                                setUser: this.setUser,
                                                login: this.state.login,
                                                setLogin: this.setLogin}}>
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
