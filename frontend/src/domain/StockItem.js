export default class StockItem {
    constructor(id, quantity, price, product) {
        this._id = id;
        this._quantity = quantity;
        this._price = price;
        this._product = product;
    }

    get id() {
        return this._id;
    }

    get name() {
        return this._product.name;
    }

    get description() {
        return this._product.description;
    }

    get quantity() {
        return this._quantity;
    }

    get price() {
        return this._price;
    }

    get imageUrl() {
        return this._product.imageUrl;
    }

    get productId() {
        return this._product.id;
    }
}