import ApiClient from "@/ApiClient.js";

export default class StocksService {
    static build() {
        return new StocksService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async fetchStocks() {
        const response = await this._apiClient.get("portal/stocks");
        return response.data;
    }

    async loadStockProducts(selectedStockId) {
        const response = await this._apiClient.get("portal/products?stockId=" + selectedStockId);
        return response.data;
    }
}