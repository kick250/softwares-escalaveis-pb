import {create} from "zustand";
import StocksService from "@/services/StocksService.js";

const stockService = StocksService.build();

export const newStore = create(((set, get) => ({
    name: '',
    loading: false,
    startNewStock() {
        set({
            name: '',
            loading: false
        });
    },
    setName(name) {
        set({name});
    },
    getErrorMessage() {
        if (!get().name) {
            return "O nome do estoque é obrigatório.";
        }
        return "";
    },
    async createStock() {
        set({ loading: true });
        const successMessage = await stockService.create(get().name);
        set({ loading: false });
        return successMessage;
    }
})));