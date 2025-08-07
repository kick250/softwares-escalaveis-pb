import {create} from "zustand";
import StocksService from "@/services/StocksService.js";

const stockService = StocksService.build();

export const deleteStore = create((set, get) => ({
    loading: false,
    async destroy(id) {
        set({loading: true});
        const successMessage = await stockService.destroy(id);
        set({loading: false});
        return successMessage;
    }
}));