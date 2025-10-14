import {create} from "zustand";
import LoginService from "@/services/LoginService.js";

const loginService = LoginService.build();

export const loginStore = create((set, get) => ({
    username: '',
    password: '',
    setUsername(username) {
        set({username})
    },
    setPassword(password) {
        set({password})
    },
    getUsername() {
        return get().username;
    },
    getPassword() {
        return get().password;
    },
    async login() {
        return await loginService.login(get().username, get().password);
    }
}));