import {useNavigate, useParams} from "react-router-dom";
import {deleteStore} from "@/store/Stocks/deleteStore.js";
import {useState} from "react";

export default function Delete() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { loading, destroy } = deleteStore();
    const [successMessage, setSuccessMessage] = useState('');

    const confirmDestroy = async () => {
        const successMessage = await destroy(id);
        setSuccessMessage(successMessage);
        setTimeout(() => navigate('/stocks'), 1500);
    }

    return (
        <div className="mt-3 d-flex flex-column align-items-center">
            {
                successMessage && <div className="alert alert-success">{successMessage}</div>
            }
            <h1>Tem certeza que deseja excluir esse estoque?</h1>
            <button className="btn btn-danger mt-3" onClick={confirmDestroy} disabled={loading}>
                Confirmar exclusão
            </button>
        </div>
    );
}