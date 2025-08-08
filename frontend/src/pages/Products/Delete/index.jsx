import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import {deleteStore} from "@/store/Products/deleteStore.js";

export default function Delete() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [successMessage, setSuccessMessage] = useState("");
    const { loading, destroyProduct } = deleteStore();

    const confirmDestroy = async () => {
        const result = await destroyProduct(id);
        setSuccessMessage(result);
        setTimeout(() => {
            navigate("/products");
        }, 1500);
    }
    return (
        <div className="mt-3 d-flex flex-column align-items-center">
            {
                successMessage && <div className="alert alert-success">{successMessage}</div>
            }
            <h1>Tem certeza que deseja excluir esse produto?</h1>
            <button className="btn btn-danger mt-3" onClick={confirmDestroy} disabled={loading}>
                Confirmar exclusão
            </button>
        </div>
    );
}