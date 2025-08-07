export default class Stock {
    constructor(id, name, stockItems) {
        this._id = id;
        this._name = name;
        this._stockItems = stockItems;
    }

    get id() {
        return this._id;
    }

    get name() {
        return this._name;
    }

    get stockItems() {
        return this._stockItems;
    }

    get length() {
        return this._stockItems.length;
    }

    toParams() {
        return {
            id: this._id,
            name: this._name,
        };
    }
}