import Stock from "@/domain/Stock.js";
import StockItemsFactory from "@/factories/StockItemsFactory.js";

export default class StocksFactory {
    static build() {
        return new StocksFactory(StockItemsFactory.build());
    }

    constructor(stockItemsFactory) {
        this._stockItemsFactory = stockItemsFactory;
    }

    createCollection(stocks) {
        return stocks.map(stock => this.create(stock));
    }

    create(stock) {
        const stockItems = this._stockItemsFactory.createCollection(stock.stockItems);
        return new Stock(
            stock.id,
            stock.name,
            stockItems
        );
    }
}