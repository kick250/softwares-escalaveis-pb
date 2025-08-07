import StockItem from "@/domain/StockItem.js";

export default class StockItemsFactory {
    static build() {
        return new StockItemsFactory();
    }

    createCollection(stockItems) {
        return stockItems.map(stockItem => this.create(stockItem));
    }

    create(stockItem) {
        return new StockItem(stockItem.id, stockItem.name, stockItem.description, stockItem.quantity, stockItem.price, stockItem.productId);
    }
}