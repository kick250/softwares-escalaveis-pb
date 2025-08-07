import Product from "@/domain/Product.js";

export default class ProductsFactory {
    static build() {
        return new ProductsFactory();
    }

    createCollection(productsData) {
        return productsData.map(productData => this.create(productData));

    }

    create(productData) {
        return new Product(
            productData.id,
            productData.name,
            productData.description
        );
    }
}