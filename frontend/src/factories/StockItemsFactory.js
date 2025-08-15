import StockItem from "@/domain/StockItem.js";
import ProductsFactory from "@/factories/ProductsFactory.js";

export default class StockItemsFactory {
    static build() {
        return new StockItemsFactory(ProductsFactory.build());
    }

    constructor(productsFactory) {
        this._productsFactory = productsFactory;
    }

    createCollection(stockItems) {
        return stockItems.map(stockItem => this.create(stockItem));
    }

    create(stockItemData) {
        const product = this._productsFactory.create(stockItemData.product);
        return new StockItem(stockItemData.id, stockItemData.quantity, stockItemData.price, product);
    }
}