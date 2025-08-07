export default class Product {
    constructor(id, name, description) {
        this._id = id;
        this._name = name;
        this._description = description;
    }

    get id() {
        return this._id;
    }

    get name() {
        return this._name;
    }

    get description() {
        return this._description;
    }
}