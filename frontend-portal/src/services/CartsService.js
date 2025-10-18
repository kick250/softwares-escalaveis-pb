import ApiClient from "@/ApiClient.js";
import UpdateCartException from "@/exceptions/UpdateCartException.js";
import LoadCartException from "@/exceptions/LoadCartException.js";

const BASE_PATH = "/portal/carts";

export default class CartsService {
    static build() {
        return new CartsService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async getCart() {
        try {
            const response = await this._apiClient.get(BASE_PATH);
            return response.data;
        } catch (error) {
            throw new LoadCartException(error.response.data);
        }
    }

    async updateCart(stockId, cartProducts) {
        try {
            const body = this._parseBody(stockId, cartProducts);
            await this._apiClient.put(BASE_PATH, body);
            return true;
        } catch (error) {
            throw new UpdateCartException(error.response.data)
        }
    }

    _parseBody(stockId, cartProducts) {
        return {
            stockId,
            items: cartProducts.map(product => ({
                itemId: product.id,
                quantity: product.quantityRequested
            }))
        }
    }
}