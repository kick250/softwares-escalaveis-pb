import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import {deleteStore} from "@/store/Stocks/Items/deleteStore.js";

export default function Delete() {
    const navigate = useNavigate();
    const { id, stockId } = useParams();
    const [successMessage, setSuccessMessage] = useState("");
    const { loading, deleteStockItem } = deleteStore();

    const confirmDestroy = async () => {
        const message = await deleteStockItem(id);
        setSuccessMessage(message);
        setTimeout(() => {
            navigate(`/stocks/${stockId}`);
        }, 1500);
    }

    const cancelDestroy = () => {
        navigate(-1);
    }

    return (
        <div className="mt-3 d-flex flex-column align-items-center">
            {
                successMessage && <div className="alert alert-success">{successMessage}</div>
            }
            <h1>Tem certeza que deseja excluir esse item?</h1>
            <button className="btn btn-danger mt-3" onClick={confirmDestroy} disabled={loading}>
                Confirmar exclusão
            </button>
            <button className="btn btn-secondary mt-3" onClick={cancelDestroy}>
                Cancelar exclusão
            </button>
        </div>
    );
}