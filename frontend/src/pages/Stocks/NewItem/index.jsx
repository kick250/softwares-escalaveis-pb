import {newItemStore} from "@/store/Stocks/newItemStore.js";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

export default function NewItem() {
    const navigate = useNavigate();
    const stockId = useParams().id;
    const {
        price, quantity, productOptions, loading,
        startNewProduct, loadProductOptions, setPrice, setQuantity, setProductId, getErrorMessage, createItem
    } = newItemStore();
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        (
            async function() {
                startNewProduct();
                await loadProductOptions();
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

    const updateProduct = ({target}) => {
        setProductId(target.value);
    };

    const checkFields = () => {
        const message = getErrorMessage();
        setErrorMessage(message);
        return !message;
    };

    const create = async () => {
        if (!checkFields()) return;
        try {
            const result = await createItem(stockId);
            setSuccessMessage(result);
            setTimeout(() => navigate(`/stocks/${stockId}`), 1500);
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Novo item</h1>
            {
                errorMessage && <span className="text-danger">{errorMessage}</span>
            }
            {
                successMessage && <span className="text-success">{successMessage}</span>
            }
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
                <div>
                    <label htmlFor="stock-product" className="form-label text-center w-100">Produto</label>
                    <select id="stock-product" name="stock-product" className="form-select" defaultValue="-1" onChange={updateProduct}>
                        <option value="-1" disabled={true}>Selecione um produto</option>
                        {productOptions.map(option => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <button className="btn btn-primary mt-3" onClick={create} disabled={loading}>
                    Criar
                </button>
            </div>
        </div>
    );
}