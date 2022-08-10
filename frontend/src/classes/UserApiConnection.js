import axios from "axios";

class UserApiConnection {

    static async  authenticate(username, password) {
        return axios.post('/auth', {
            login: username,
            password: password
        })
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                throw error;
            });
    }

    static async register(username, password) {
        return axios.post('/register', {
            login: username,
            password: password
        })
            .then(function (response) {
                return response.data;
            })
            .catch(function (error) {
                throw error;
            });
    }

}

export default UserApiConnection;