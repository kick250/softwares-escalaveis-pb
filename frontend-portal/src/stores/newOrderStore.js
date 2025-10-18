import {create} from "zustand";
import StocksService from "@/services/StocksService.js";
import OrdersService from "@/services/OrdersService.js";
import CartsService from "@/services/CartsService.js";

const cartsService = CartsService.build();
const stocksService = StocksService.build();
const ordersService = OrdersService.build();


export const newOrderStore = create((set, get) => ({
    stocks: [],
    selectedStockId: -1,
    stockProducts: [],
    cartProducts: {},
    async loadCart() {
        const cart = await cartsService.getCart();

        if (cart.items.length === 0) return;

        await get().loadProducts(cart.stockId);
        const cartProducts = {};
        cart.items.forEach(item => {
            const product = get().stockProducts.find(p => p.id === item.itemId);
            cartProducts[item.id] = {...product, id: item.itemId, quantityRequested: item.quantity};
        });
        set({selectedStockId: cart.stockId, cartProducts});
    },
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
    getProductQuantity(product) {
        const cartProducts = get().cartProducts;
        return cartProducts[product.id] ? cartProducts[product.id].quantityRequested : 0;
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
    async updateCart() {
        await cartsService.updateCart(get().selectedStockId, get().getCartProducts());
        return true;
    },
    clearCart() {
        set({selectedStockId: -1, cartProducts: {}, stockProducts: []});
    },
    async sendOrder() {
        const products = get().getCartProducts();
        return await ordersService.create(get().selectedStockId, products)
    }
}));