import {create} from "zustand";
import ProductsService from "@/services/ProductsService.js";
import ProductsFactory from "@/factories/ProductsFactory.js";
import StockItemsService from "@/services/StockItemsService.js";

const productsService = ProductsService.build();
const productsFactory = ProductsFactory.build();
const stockItemService = StockItemsService.build();

export const newItemStore = create(((set, gets) => ({
    price: '',
    quantity: '',
    productId: undefined,
    productOptions: [],
    loading: false,
    startNewProduct() {
        set({
            price: '',
            quantity: '',
            productId: undefined,
            loading: false
        });
    },
    async loadProductOptions() {
        const data = await productsService.getAll();
        const products = productsFactory.createCollection(data.products);
        const productOptions = products.map(product => ({
            value: product.id,
            label: product.name
        }));
        set({productOptions});
    },
    setPrice(price) {
        set({price: price});
    },
    setQuantity(quantity) {
        set({quantity: quantity});
    },
    setProductId(productId) {
        set({productId: parseInt(productId)});
    },
    getErrorMessage() {
        const {price, quantity, productId} = gets();
        if (!price || isNaN(parseFloat(price)) || parseFloat(price) <= 0) return "Preço é obrigatório e deve ser um número válido.";
        if (!quantity || isNaN(parseInt(quantity)) || parseInt(quantity) <= 0) return "Quantidade é obrigatória e deve ser um inteiro válido.";
        if (!productId) return "Produto é obrigatório.";
        return "";
    },
    async createItem(stockId) {
        const {price, quantity, productId} = gets();
        set({loading: true});

        try {
            return await stockItemService.create(stockId, price, quantity, productId);
        } catch (error) {
            throw error;
        } finally {
            set({loading: false});
        }
    }
})));