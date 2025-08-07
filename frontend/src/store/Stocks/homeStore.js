import { create } from 'zustand';
import StocksService from "@/services/StocksService.js";
import StocksFactory from "@/factories/StocksFactory.js";

const stocksService = StocksService.build();
const stockFactory = StocksFactory.build();

export const homeStore = create((set) => ({
    stocks: [],
    async loadStocks() {
        const stocks = await stocksService.getAll();
        set({ stocks: stockFactory.createCollection(stocks) });
    }
}));