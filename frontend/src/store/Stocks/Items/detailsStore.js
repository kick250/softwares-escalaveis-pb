import {create} from "zustand";
import StockItemsService from "@/services/StockItemsService.js";
import StockItemsFactory from "@/factories/StockItemsFactory.js";
import StocksService from "@/services/StocksService.js";
import StocksFactory from "@/factories/StocksFactory.js";

const stockItemService = StockItemsService.build();
const stockItemFactory = StockItemsFactory.build();
const stockService = StocksService.build();
const stockFactory = StocksFactory.build();

export const detailsStore = create((set => ({
    stockItem: undefined,
    stock: undefined,
    async loadStockItem(id) {
        const data = await stockItemService.getById(id);
        set({stockItem: stockItemFactory.create(data)});
    },
    async loadStock(stockId) {
        const data = await stockService.getById(stockId);
        set({stock: stockFactory.create(data)});
    }
})));