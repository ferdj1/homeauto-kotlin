import axios from 'axios'

export const AXIOS = axios.create({
    baseURL: `http://192.168.100.54:8080/api`,
    headers: {
        'Access-Control-Allow-Origin': 'http://192.168.100.54:8081'
    }
})