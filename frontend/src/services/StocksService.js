import ApiClient from "@/ApiClient.js";
import StockOperationErrorResponse from "@/Errors/StockOperationErrorResponse.js";

const PATH = "/stocks";

export default class StocksService {
    static build () {
        return new StocksService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async getAll() {
        try {
            const response = await this._apiClient.get(PATH);
            return response.data.stocks;
        } catch (error) {
            throw new StockOperationErrorResponse(error.response.data);
        }
    }

    async getById(id) {
        try {
            const response = await this._apiClient.get(`${PATH}/${id}`);
            return response.data;
        } catch (error) {
            throw new StockOperationErrorResponse(error.response.data);
        }
    }

    async create(name) {
        try {
            const response = await this._apiClient.post(PATH, { name });
            return response.data;
        } catch (error) {
            throw new StockOperationErrorResponse(error.response.data);
        }
    }

    async update(id, name) {
        try {
            const response = await this._apiClient.put(`${PATH}/${id}`, { name });
            return response.data;
        } catch (error) {
            throw new StockOperationErrorResponse(error.response.data);
        }
    }

    async destroy(id) {
        try {
            const response = await this._apiClient.delete(`${PATH}/${id}`);
            return response.data;
        } catch (error) {
            throw new StockOperationErrorResponse(error.response.data);
        }
    }
}