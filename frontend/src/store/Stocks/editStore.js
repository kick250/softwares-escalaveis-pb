import {create} from "zustand";
import StocksService from "@/services/StocksService.js";
import StocksFactory from "@/factories/StocksFactory.js";

const stockService = StocksService.build();
const stockFactory = StocksFactory.build();

export const editStore = create((set, get) => ({
    stock: null,
    stockParams: {},
    loading: true ,
    loadingUpdate: false,
    async loadStock(id){
        set({loading: true});
        const stockData = await stockService.getById(id);
        const stock = stockFactory.create(stockData);
        set({stock, stockParams: stock.toParams(), loading: false});
    },
    updateName(name) {
        set((state)({ stockParams: { ...state.stockParams, name } }));
    },
    async updateStock() {
        set({loadingUpdate: true});
        const { stock, stockParams } = get();
        const successMessage = await stockService.update(stock.id, stockParams.name);
        set({ loadingUpdate: false});
        return successMessage;
    }
}));