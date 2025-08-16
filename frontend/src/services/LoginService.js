import ApiClient from "@/ApiClient.js";

export default class LoginService {
    static build() {
        return new LoginService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async login(username, password) {
        try {
            const response = await this._apiClient.post('/login', { username, password });
            return response.data.token;
        } catch (error) {
            return undefined;
        }
    }
}