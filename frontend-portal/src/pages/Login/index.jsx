import {authStore} from "@/stores/authStore.js";
import {loginStore} from "@/stores/loginStore.js";
import {useState} from "react";

const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export default function Login() {
    const { setToken } = authStore();
    const { setUsername, setPassword, getUsername, getPassword, login } = loginStore();
    const [errorMessage, setErrorMessage] = useState("");

    const usernameFieldHandler = (event) => {
        setUsername(event.target.value);
    };

    const passwordFieldHandler = (event) => {
        setPassword(event.target.value);
    };

    const submitHandler = async (event) => {
        event.preventDefault();
        setErrorMessage("");

        if (hasErrorMessage()) return setErrorMessage(getErrorMessage());

        try {
            const token = await login();
            setToken(token);
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const hasErrorMessage = () => {
        return !!getErrorMessage();
    }

    const getErrorMessage = () => {
        if (!getUsername() || !emailRegex.test(getUsername())) return "Email inválido.";
        if (!getPassword()) return "Senha não pode ser vazia.";
        if (getPassword().length < 6) return "Senha deve ter pelo menos 6 caracteres.";
        return "";
    }

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="d-flex flex-column align-items-center w-50">
                <div className="text-center">
                    <img src="/vite.svg" alt="Logo" width="auto" height="60px"/>
                    <h1>Portal</h1>
                </div>
                <div className="bg-light p-4 rounded shadow w-50 text-center">
                    {errorMessage && <div className="alert alert-danger p-1" role="alert">{errorMessage}</div>}
                    <label htmlFor="username">Email</label>
                    <input type="email" className="form-control my-2" id="username" name="username" placeholder="Seu email" onInput={usernameFieldHandler} />
                    <label htmlFor="password">Password</label>
                    <input type="password" className="form-control my-2" id="password" name="passoword" placeholder="Sua senha" onInput={passwordFieldHandler} />
                    <button className="btn btn-primary w-100 mt-3" onClick={submitHandler}>
                        Entrar
                    </button>
                </div>
            </div>
        </div>
    );
}