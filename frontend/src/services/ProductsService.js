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

    async getById(id) {
        try {
            const response = await this._apiClient.get(`/products/${id}`);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }

    async create(name, description, image) {
        try {
            const formData = new FormData();
            formData.append('name', name);
            formData.append('description', description);
            formData.append('image', image);

            const response = await this._apiClient.post('/products', formData, {headers: {'Content-Type': 'multipart/form-data'}});
            return response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async update(id, newName, newDescription, newImage) {
        try {
            const formData = new FormData();
            formData.append('name', newName);
            formData.append('description', newDescription);
            if (newImage) {
                formData.append('image', newImage);
            }

            const response = await this._apiClient.put(`/products/${id}`, formData, {headers: {'Content-Type': 'multipart/form-data'}});
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }

    async destroy(id) {
        try {
            const response = await this._apiClient.delete(`/products/${id}`);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    }
}