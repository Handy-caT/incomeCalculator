import {Cookies} from "react-cookie";

class UserModel {

    constructor(name, cookieAgreement) {
        this.name = name;
        this.cookieAgreement = cookieAgreement;
    }

    setCookieAgreement(cookieAgreement) {
        this.cookieAgreement = cookieAgreement;
    }

    setToken(token) {
        this.token = token;
    }

    setCookieToken(token) {
        this.cookieToken = token;
    }

    saveSession() {
        sessionStorage.setItem("name", this.name);
        sessionStorage.setItem("cookieAgreement", this.cookieAgreement);
        sessionStorage.setItem("token", this.token);
    }

    saveCookie() {
        const cookies = new Cookies();
        cookies.set("name", this.name, {path: "/"});
        cookies.set("cookieAgreement", this.cookieAgreement, {path: "/"});
        cookies.set("token", this.token, {path: "/"});
    }

    deleteCookie() {
        const cookies = new Cookies();
        cookies.remove("name", {path: "/"});
        cookies.remove("cookieAgreement", {path: "/"});
        cookies.remove("token", {path: "/"});
    }

    deleteSession() {
        sessionStorage.clear();
    }

    loadSession() {
       this.name = sessionStorage.getItem("name");
       this.cookieAgreement = sessionStorage.getItem("cookieAgreement");
       this.token = sessionStorage.getItem("token");

       console.log(this.name != null);
       return this.name != null;
    }

    loadCookie() {
        const cookies = new Cookies();
        this.name = cookies.get("name");
        this.cookieAgreement = cookies.get("cookieAgreement");
        this.token = cookies.get("token");

        return this.name != null;
    }
}


export default UserModel;