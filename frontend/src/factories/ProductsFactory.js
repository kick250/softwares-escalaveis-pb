import Product from "@/domain/Product.js";

export default class ProductsFactory {
    static build() {
        return new ProductsFactory(import.meta.env.VITE_API_URL);
    }

    constructor(apiUrl) {
        this._apiUrl = apiUrl;
    }

    createCollection(productsData) {
        return productsData.map(productData => this.create(productData));

    }

    create(productData) {
        const imageUrl = this._apiUrl + productData.imagePath

        return new Product(
            productData.id,
            productData.name,
            productData.description,
            imageUrl
        );
    }
}