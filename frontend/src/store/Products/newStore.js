import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";

const productsService = ProductsService.build();

export const newStore = create(((set, get) => ({
    productData: {
        name: "",
        description: "",
        image: undefined
    },
    loading: false,
    startNewProduct() {
        set({
            productData: {
                name: "",
                description: "",
                image: undefined
            },
            loading: false
        });
    },
    setName(name) {
        set(state => ({productData: {...state.productData, name}}));
    },
    setDescription(description) {
        set(state => ({productData: {...state.productData, description}}));
    },
    setImage(image) {
        set(state => ({productData: {...state.productData, image}}));
    },
    getErrorMessage() {
        const {name, description, image} = get().productData;
        if (!name) return "O nome do produto é obrigatório.";
        if (!description) return "A descrição do produto é obrigatória.";
        if (!image) return "A imagem do produto é obrigatória.";

        return "";
    },
    async createProduct() {
        set({loading: true});
        const {name, description, image} = get().productData;
        const result = await productsService.create(name, description, image);
        set({loading: false});
        return result;
    }
})));