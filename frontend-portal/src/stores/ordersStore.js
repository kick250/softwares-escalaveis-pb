import {create} from "zustand";
import OrdersService from "@/services/OrdersService.js";

const ordersService = OrdersService.build();

export const ordersStore = create((set, get) => ({
    orders: [],
    getOrders() {
        return get().orders;
    },
    async loadOrders() {
        const orders = await ordersService.fetchOrders();
        set({orders})
    }
}));