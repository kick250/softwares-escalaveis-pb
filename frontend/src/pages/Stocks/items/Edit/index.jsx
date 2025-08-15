import {useEffect, useState} from "react";
import {editStore} from "@/store/Stocks/Items/editStore.js";
import {useNavigate, useParams} from "react-router-dom";

export default function Edit() {
    const navigate = useNavigate();
    const { id, stockId} = useParams();
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const { price, quantity, loadingUpdate, loadStockItem, setPrice, setQuantity, getErrorMessage, updateStockItem } = editStore();

    useEffect(() => {
        (
            async () => {
                await loadStockItem(id);
                setLoading(false);
            }
        )()
    }, []);

    const updatePrice = ({target}) => {
        const value = target.value.replace(/[^0-9.]/g, '');
        const parts = value.split('.');
        if (parts.length > 2) return;

        setPrice(value);
    };

    const updateQuantity = ({target}) => {
        const value = target.value.replace(/[^0-9]/g, '');
        setQuantity(value);
    };

    const checkFields = () => {
        const message = getErrorMessage();
        setErrorMessage(message);
        return !message;
    };

    const update = async () => {
        if (!checkFields()) return;
        try {
            const result = await updateStockItem();
            setSuccessMessage(result);
            setTimeout(() => navigate(`/stocks/${stockId}/items/${id}`), 1500);
        } catch (error) {
            setErrorMessage(error.message);
        }
    }

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Atualizar dados do item</h1>
            {
                errorMessage && <span className="text-danger">{errorMessage}</span>
            }
            {
                successMessage && <span className="text-success">{successMessage}</span>
            }

            {
                loading ?
                    <div className="spinner-border text-primary mt-5"></div>
                    :
                    <div className="mt-3 d-flex flex-column bg-light w-25 py-3 px-5 rounded-3 shadow">
                        <div className="mb-2">
                            <label htmlFor="stock-price" className="form-label text-center w-100">Preço</label>
                            <div className="d-flex align-items-center">
                                <span className="me-2">R$</span>
                                <input id="stock-price" name="stock-price" type="text" className="form-control" value={price} onInput={updatePrice} placeholder="Preço"/>
                            </div>
                        </div>
                        <div className="mb-2">
                            <label htmlFor="stock-quantity" className="form-label text-center w-100">Quantidade</label>
                            <input id="stock-quantity" name="stock-quantity" type="text" className="form-control" placeholder="Quantidade"
                                   value={quantity}
                                   onInput={updateQuantity}
                            />
                        </div>
                        <button className="btn btn-primary mt-3" onClick={update} disabled={loadingUpdate}>
                            Atualizar
                        </button>
                    </div>
            }
        </div>
    );
}