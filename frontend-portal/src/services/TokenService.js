export default class TokenService {
    static build() {
        return new TokenService();
    }

    storeToken(token) {
        localStorage.setItem("portalToken", token);
    }

    getToken()  {
        return localStorage.getItem("portalToken");
    }

    logout() {
        localStorage.removeItem("portalToken");
    }
}