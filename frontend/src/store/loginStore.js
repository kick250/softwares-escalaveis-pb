import {create} from "zustand";
import LoginService from "@/services/LoginService.js";

const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const loginService = LoginService.build();

export const loginStore = create((set, gets) => ({
    username: '',
    password: '',
    loading: false,
    setUsername(username) {
        set({username});
    },
    setPassword(password) {
        set({password});
    },
    getErrorMessage() {
        const {username, password} = gets();

        if (!username || !emailRegex.test(username)) return "Email inválido";
        if (!password) return "Senha não pode ser vazia";
        if (password.length < 6) return "Senha deve ter pelo menos 6 caracteres";
        return "";
    },
    login() {
        const {username, password} = gets();

        set({loading: true});
        const token = loginService.login(username, password);
        set({loading: false});
        return token;
    },
    clearCredentials() {
        set({username: '', password: ''});
    },
}));