import {newStore} from "@/store/Stocks/newStore.js";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

export default function New() {
    const navigate = useNavigate();
    const { loading, setName, createStock } = newStore();
    const [successMessage, setSuccessMessage] = useState("");

    const updateName = ({target}) => {
        setName(target.value);
    };

    const create = async () => {
        const successMessage = await createStock();
        setSuccessMessage(successMessage);
        setTimeout(() => navigate("/stocks"), 1500);
    };

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Criar novo estoque</h1>
            {
                successMessage && <span className="text-success">{successMessage}</span>
            }
            <div className="d-flex flex-column bg-light w-25 py-3 px-5 rounded-3 shadow">
                <div>
                    <label htmlFor="stock-name" className="form-label">Nome</label>
                    <input id="stock-name" name="stock-name" className="form-control" onInput={updateName} placeholder="Nome"/>
                </div>
                <button className="btn btn-primary mt-3" onClick={create} disabled={loading}>
                    Criar
                </button>
            </div>
        </div>
    );
}