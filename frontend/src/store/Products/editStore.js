import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";
import ProductsFactory from "@/factories/ProductsFactory.js";

const productsService = ProductsService.build();
const productsFactory = ProductsFactory.build();

export const editStore = create(((set, get) => ({
    product: undefined,
    newName: '',
    newDescription: '',
    newImage: undefined,
    loading: true,
    loadingUpdate: false,
    async loadProduct(id) {
        const data = await productsService.getById(id);
        const product = productsFactory.create(data);
        set({product, newName: product.name, newDescription: product.description, loading: false});
    },
    setNewName(newName) {
        set({newName});
    },
    setNewDescription(newDescription) {
        set({newDescription});
    },
    setNewImage(newImage) {
        set({newImage});
    },
    getErrorMessage() {
        const {newName, newDescription, newImage} = get();
        if (!newName) return "O nome do produto é obrigatório.";
        if (!newDescription) return "A descrição do produto é obrigatória.";

        return "";
    },
    async updateProduct() {
        const {product, newName, newDescription, newImage} = get();
        set({loadingUpdate: true});
        const successMessage = await productsService.update(product.id, newName, newDescription, newImage);
        set({loadingUpdate: false});
        return successMessage;
    }
})));