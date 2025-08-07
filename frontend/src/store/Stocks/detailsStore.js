import {create} from "zustand";
import StocksService from "@/services/StocksService.js";
import StocksFactory from "@/factories/StocksFactory.js";

const stockService = StocksService.build();
const stockFactory = StocksFactory.build();

export const detailsStore = create(((set, get) => ({
    stock: null,
    loading: true,
    async loadStock(id) {
        set({loading: true});
        const stock = await stockService.getById(id);
        set({stock: stockFactory.create(stock), loading: false});
    }
})));