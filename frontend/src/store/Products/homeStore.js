import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";
import ProductsFactory from "@/factories/ProductsFactory.js";

const productsService = ProductsService.build();
const productsFactory = ProductsFactory.build();

export const homeStore = create((set) => ({
    products: [],
    async loadProducts() {
        const data = await productsService.getAll();
        set({products: productsFactory.createCollection(data.products)});
    }
}));