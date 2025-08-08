import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";

const productService = ProductsService.build();

export const deleteStore = create((set) => ({
    loading: false,
    async destroyProduct(id) {
        set({loading: true});
        const successMessage = await productService.destroy(id);
        set({loading: false});
        return successMessage;
    }
}));