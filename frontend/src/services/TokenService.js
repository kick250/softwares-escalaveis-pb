export default class TokenService {
    static build() {
        return new TokenService();
    }

    storeToken(token) {
        localStorage.setItem("token", token);
    }

    getToken()  {
        return localStorage.getItem("token");
    }

    logout() {
        localStorage.removeItem("token");
    }
}