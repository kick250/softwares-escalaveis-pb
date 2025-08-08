export default class Product {
    constructor(id, name, description, imageUrl) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._imageUrl = imageUrl;
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

    get imageUrl() {
        return this._imageUrl;
    }
}