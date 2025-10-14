import axios from "axios";
import TokenService from "@/services/TokenService.js";

const tokenService = TokenService.build();

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use(
    (config) => {
        const token = tokenService.getToken();

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response) {
            if (error.response.status === 403) {
                tokenService.logout();
                window.location.reload();
            }
        }
        return Promise.reject(error);
    }
);

export default api;