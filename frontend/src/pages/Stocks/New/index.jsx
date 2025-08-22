import {newStore} from "@/store/Stocks/newStore.js";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

export default function New() {
    const navigate = useNavigate();
    const { loading, startNewStock, setName, getErrorMessage, createStock } = newStore();
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const updateName = ({target}) => {
        setName(target.value);
    };

    useEffect(() => {
        startNewStock();
    }, []);

    const checkFields = () => {
        const message = getErrorMessage();
        if (message) {
            setErrorMessage(message);
            return false;
        }
        return true;
    }

    const create = async () => {
        try {
            if (!checkFields()) return;

            const successMessage = await createStock();
            setSuccessMessage(successMessage);
            setTimeout(() => navigate("/stocks"), 1500);
        } catch (error) {
            alert(error.message);
        }
    };

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Criar novo estoque</h1>
            {
                errorMessage && <span className="text-danger">{errorMessage}</span>
            }
            {
                successMessage && <span className="text-success">{successMessage}</span>
            }
            <div className="d-flex flex-column bg-light w-25 py-3 px-5 rounded-3 shadow">
                <div>
                    <label htmlFor="stockEntity-name" className="form-label">Nome</label>
                    <input id="stockEntity-name" name="stockEntity-name" className="form-control" onInput={updateName} placeholder="Nome"/>
                </div>
                <button className="btn btn-primary mt-3" onClick={create} disabled={loading}>
                    Criar
                </button>
            </div>
        </div>
    );
}