import axios from "axios";

export async function authenticate(username, password) {
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

export async function register(username, password) {
    return axios.post('/register', {
        login: username,
        password: password
    })
        .then(function (response) {
            response.headers;
            return response.data;
        })
        .catch(function (error) {
            throw error;
        });
}

export async function getUser(id) {
    return axios.get('/user/' + id, {headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}})
        .then(function (response) {
            
            return response;
        })
        .catch(function (error) {
            throw error;
        });
}

export async function getUserMe() {
    return axios.get('/user/me', {headers: {'Authorization': 'Bearer ' + localStorage.getItem('token')}})
        .then(function (response) {
            return response;
        })
        .catch(function (error) {
            throw error;
        });
}





