import ApiClient from "@/ApiClient.js";
import OrderApproveException from "@/exceptions/OrderApproveException.js";

const BASE_PATH = "/portal/orders";

export default class OrdersService {
    static build() {
        return new OrdersService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async fetchOrders() {
        const response = await this._apiClient.get(BASE_PATH);
        return response.data;
    }

    async getById(id) {
        const response = await this._apiClient.get(`${BASE_PATH}/${id}`);
        return response.data;
    }

    async create(stockId, products) {
        return await this._apiClient.post(BASE_PATH, this._createSerializedBody(stockId, products));
    }

    _createSerializedBody(stockId, products) {
        return {
            stockId,
            items: products.map((product) => ({ itemId: product.id, quantity: product.quantityRequested })),
        };
    }

    async approve(id) {
        try {
            const response = await this._apiClient.post(`${BASE_PATH}/${id}/approve`);
            return response.data;
        } catch (error) {
            throw new OrderApproveException(error.response.data);
        }
    }

    async cancel(id) {
        try {
            const response = await this._apiClient.post(`${BASE_PATH}/${id}/cancel`);
            return response.data;
        } catch (error) {
            throw new OrderApproveException(error.response.data);
        }
    }
}