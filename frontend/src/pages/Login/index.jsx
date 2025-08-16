import {loginStore} from "@/store/loginStore.js";
import {useState} from "react";
import {authStore} from "@/store/authStore.js";


export default function Login() {
    const { setToken } = authStore();
    const { loading, setUsername, setPassword, login, getErrorMessage } = loginStore();
    const [errorMessage, setErrorMessage] = useState('');

    const updateUsername = ({target}) => {
        setUsername(target.value);
    };

    const updatePassword = ({target}) => {
        setPassword(target.value);
    };

    const checkFields = () => {
        setErrorMessage("");
        const message = getErrorMessage();
        if (message) {
            setErrorMessage(message);
            return false;
        }
        return true;
    };

    const handleLogin = async () => {
        if (!checkFields()) return;

        const token = await login();

        if (!token) return setErrorMessage("Usuario ou senha inválidos");

        setToken(token);
    };

    return (
        <div className="d-flex flex-column justify-content-center align-items-center vh-100">
            <h1><img src="/vite.svg" alt="Logo" width="auto" height="60px"/></h1>
            {
                errorMessage && (
                    <div className="alert alert-danger w-25" role="alert">
                        {errorMessage}
                    </div>
                )
            }
            <div className="bg-light p-5 rounded shadow w-25">
                <div className="text-center">
                    <label className="mb-2" htmlFor="username">Email</label>
                    <input id="username" name="username" placeholder="Email" type="email" className="form-control text-center" onInput={updateUsername} />
                </div>
                <div className="text-center mt-3">
                    <label className="mb-2" htmlFor="password">Senha</label>
                    <input id="password" name="password" placeholder="Senha" type="password" className="form-control text-center" onInput={updatePassword} />
                </div>
                <button className="btn btn-primary form-control mt-3" disabled={loading} onClick={handleLogin}>Entrar</button>
            </div>
        </div>
    );
}