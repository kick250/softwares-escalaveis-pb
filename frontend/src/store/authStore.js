import {create} from "zustand";
import TokenService from "@/services/TokenService.js";

const tokenService = TokenService.build()

export const authStore = create((set, get) => ({
    token: '',
    isAuthenticated() {
        return !!get().token;
    },
    setToken(token) {
        tokenService.storeToken(token);
        set({token});
    },
    loadSession() {
        const token = tokenService.getToken();
        if (token) set({token});
    },
    logout() {
        tokenService.logout();
        set({token: ''});
    }
}));