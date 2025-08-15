import ApiClient from "@/ApiClient.js";
import StockItemOperationErrorResponse from "@/Errors/StockItemOperationErrorResponse.js";

export default class StockItemsService {
    static build () {
        return new StockItemsService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async create(stockId, price, quantity, productId) {
        try {
            const body = { price, quantity, productId, stockId };
            const response = await this._apiClient.post(`/stock_items`, body);
            return response.data;
        } catch (error) {
            throw new StockItemOperationErrorResponse(error.response.data.message);
        }
    }

    async getById(id) {
        try {
            const response = await this._apiClient.get(`/stock_items/${id}`);
            return response.data;
        } catch (error) {
            throw new StockItemOperationErrorResponse(error.response.data.message);
        }
    }

    async update(id, price, quantity) {
        try {
            const body = { price, quantity };
            const response = await this._apiClient.put(`/stock_items/${id}`, body);
            return response.data;
        } catch (error) {
            throw new StockItemOperationErrorResponse(error.response.data.message);
        }
    }

    async destroy(id) {
        try {
            const response = await this._apiClient.delete(`/stock_items/${id}`);
            return response.data;
        } catch (error) {
            throw new StockItemOperationErrorResponse(error.response.data.message);
        }
    }
}