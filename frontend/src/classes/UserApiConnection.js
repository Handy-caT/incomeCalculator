import axios from "axios";

class UserApiConnection {


    authenticate(username, password) {
        return axios.post('/auth', {
            login: username,
            password: password
        })
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                return error;
            });
    }

    register(username, password) {
        return axios.post('/register', {
            login: username,
            password: password
        })
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                return error;
            });
    }

}