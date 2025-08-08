import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";
import ProductsFactory from "@/factories/ProductsFactory.js";

const productsService = ProductsService.build();
const productsFactory = ProductsFactory.build();

export const detailsStore = create((set => ({
    loading: true,
    product: undefined,
    async loadProduct(id) {
        set({loading: true});
        const data = await productsService.getById(id)
        set({loading: false, product: productsFactory.create(data)});
    }
})));