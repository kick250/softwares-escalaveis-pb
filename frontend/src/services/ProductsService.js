import ApiClient from "@/ApiClient.js";

export default class ProductsService {
    static build() {
        return new ProductsService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async getAll() {
        try {
            const response = await this._apiClient.get('/products');
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }
}