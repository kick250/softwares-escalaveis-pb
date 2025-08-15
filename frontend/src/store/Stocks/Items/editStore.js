import {create} from "zustand";
import StockItemsService from "@/services/StockItemsService.js";
import StockItemsFactory from "@/factories/StockItemsFactory.js";

const stockItemsService = StockItemsService.build()
const stockItemsFactory = StockItemsFactory.build();

export const editStore = create((set, gets) => ({
    stockItem: undefined,
    price: '',
    quantity: '',
    loadingUpdate: false,
    async loadStockItem(id) {
        const data = await stockItemsService.getById(id);
        const stockItem = stockItemsFactory.create(data);
        set({stockItem, price: stockItem.price, quantity: stockItem.quantity});
    },
    setPrice(price) {
        set({price})
    },
    setQuantity(quantity) {
        set({quantity})
    },
    getErrorMessage() {
        const {price, quantity} = gets();
        if (!price || isNaN(parseFloat(price)) || parseFloat(price) <= 0) return "Preço é obrigatório e deve ser um número válido.";
        if (!quantity || isNaN(parseInt(quantity)) || parseInt(quantity) <= 0) return "Quantidade é obrigatória e deve ser um inteiro válido.";
        return "";
    },
    async updateStockItem() {
        const {stockItem, price, quantity} = gets();
        set({loadingUpdate: true});
        try {
            return await stockItemsService.update(stockItem.id, price, quantity);
        } catch (error) {
            throw error;
        } finally {
            set({loadingUpdate: false});
        }
    }
}));