import ApiClient from "@/ApiClient.js";

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
            console.log(error);
        }
    }

    async getById(id) {
        try {
            const response = await this._apiClient.get(`${PATH}/${id}`);
            return response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async create(name) {
        try {
            const response = await this._apiClient.post(PATH, { name });
            return response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async update(id, name) {
        try {
            const response = await this._apiClient.put(`${PATH}/${id}`, { name });
            return response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async destroy(id) {
        try {
            const response = await this._apiClient.delete(`${PATH}/${id}`);
            return response.data;
        } catch (error) {
            console.log(error);
        }
    }
}