import {create} from "zustand";
import StockItemsService from "@/services/StockItemsService.js";

const stockItemsService = StockItemsService.build();

export const deleteStore = create((set => ({
    loading: false,
    async deleteStockItem(id) {
        set({loading: true})
        try {
            return await stockItemsService.destroy(id)
        } catch (error) {
            throw error;
        } finally {
            set({loading: false})
        }
    }
})));