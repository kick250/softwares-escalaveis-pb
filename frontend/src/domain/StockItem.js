export default class StockItem {
    constructor(id, name, description, quantity, price, productId) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._quantity = quantity;
        this._price = price;
        this._productId = productId;
    }
}