import ApiClient from "@/ApiClient.js";
import LoginException from "@/exceptions/LoginException.js";

export default class LoginService {
    static build() {
        return new LoginService(ApiClient);
    }

    constructor(apiClient) {
        this._apiClient = apiClient;
    }

    async login(username, password) {
        try {
            const response = await this._apiClient.post("/login", { username, password });
            return response.data.token;
        } catch {
            throw new LoginException("Usuário ou senha inválidos. Se o problema persistir, contate o administrador.");
        }
    }
}