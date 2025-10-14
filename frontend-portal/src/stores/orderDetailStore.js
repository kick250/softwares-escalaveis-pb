import {create} from "zustand";
import OrdersService from "@/services/OrdersService.js";

const ordersService = OrdersService.build();

export const orderDetailStore = create((set => ({
    order: undefined,
    async loadOrder(id) {
        const data = await ordersService.getById(id);
        set({order: data})
    },
    async approveOrder(id) {
        return await ordersService.approve(id);
    },
    async cancelOrder(id) {
        return await ordersService.cancel(id);
    }
})));