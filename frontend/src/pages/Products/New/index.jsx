import {useEffect, useState} from "react";
import {newStore} from "@/store/Products/newStore.js";
import {useNavigate} from "react-router-dom";

export default function New() {
    const navigate = useNavigate();
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const { loading, startNewProduct, setName, setDescription, setImage, getErrorMessage, createProduct } = newStore();

    useEffect(() => {
        startNewProduct();
    }, []);

    const updateName = ({target}) => {
        setName(target.value);
    };

    const updateDescription = ({target}) => {
        setDescription(target.value);
    };

    const updateImage = ({target}) => {
        setImage(target.files[0]);
    };

    const checkFields = () => {
        const message = getErrorMessage();
        setErrorMessage(message);
        return !message;
    };

    const create = async () => {
        if (!checkFields()) return;

        const result = await createProduct();
        setSuccessMessage(result);
        setTimeout(() => navigate("/products"), 3000);
    };

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Criar novo produto</h1>
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
                <div className="mt-1">
                    <label htmlFor="stockEntity-description" className="form-label mt-3">Descrição</label>
                    <textarea id="stockEntity-description" name="stockEntity-description" className="form-control" onInput={updateDescription} placeholder="Descrição" />
                </div>
                <div className="mt-1">
                    <label htmlFor="stockEntity-image" className="form-label mt-3">Imagem</label>
                    <input id="stockEntity-image" name="stockEntity-image" className="form-control" onInput={updateImage} type="file" accept="image/*" />
                </div>
                <button className="btn btn-primary mt-3" onClick={create} disabled={loading}>
                    Criar
                </button>
            </div>
        </div>
    );
};