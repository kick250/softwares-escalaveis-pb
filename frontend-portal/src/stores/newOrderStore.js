import {create} from "zustand";
import StocksService from "@/services/StocksService.js";
import OrdersService from "@/services/OrdersService.js";

const stocksService = StocksService.build();
const ordersService = OrdersService.build()

export const newOrderStore = create((set, get) => ({
    stocks: [],
    selectedStockId: undefined,
    stockProducts: [],
    cartProducts: {},
    getStocks() {
        return get().stocks;
    },
    getStockProducts() {
        return get().stockProducts;
    },
    getCartProducts() {
        return Object.values(get().cartProducts);
    },
    async loadProducts(selectedStockId) {
        const stockProducts = await stocksService.loadStockProducts(selectedStockId);
        set({selectedStockId, stockProducts});
    },
    async loadStocks() {
        const stocks = await stocksService.fetchStocks();
        set({stocks});
    },
    setProductQuantity(product, quantity) {
        const cartProducts = {...get().cartProducts};
        if (quantity > 0) {
            cartProducts[product.id] = {...product, quantityRequested: quantity};
        } else {
            delete cartProducts[product.id];
        }
        set({cartProducts});
    },
    clearCart() {
        set({selectedStockId: -1, cartProducts: {}, stockProducts: []});
    },
    async sendOrder() {
        const products = get().getCartProducts();
        return await ordersService.create(get().selectedStockId, products)
    }
}));