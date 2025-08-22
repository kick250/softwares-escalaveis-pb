import {useEffect, useState} from "react";
import {editStore} from "@/store/Products/editStore.js";
import {useNavigate, useParams} from "react-router-dom";

export default function Edit() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const {
        newName, newDescription, newImage, loading, loadingUpdate,
        loadProduct, setNewName, setNewDescription, setNewImage, getErrorMessage, updateProduct
    } = editStore();

    useEffect(() => {
        (async () => {
            await loadProduct(id);
        })();
    }, []);

    const updateName = ({target}) => {
        setNewName(target.value);
    }

    const updateDescription = ({target}) => {
        setNewDescription(target.value);
    }

    const updateImage = ({target}) => {
        setNewImage(target.files[0]);
    }

    const checkFields = () => {
        const message = getErrorMessage();
        setErrorMessage(message);
        return !message;
    };

    const update = async () => {
        if (!checkFields()) return;

        const result = await updateProduct();
        setSuccessMessage(result)
        setTimeout(() => navigate(`/products/${id}`), 3000);
    }

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
                    <input id="stockEntity-name" name="stockEntity-name" className="form-control" value={newName} onInput={updateName} placeholder="Nome"/>
                </div>
                <div className="mt-1">
                    <label htmlFor="stockEntity-description" className="form-label mt-3">Descrição</label>
                    <textarea id="stockEntity-description" name="stockEntity-description" className="form-control" rows={3} value={newDescription} onInput={updateDescription} placeholder="Descrição" />
                </div>
                <div className="mt-1">
                    <label htmlFor="stockEntity-image" className="form-label mt-3">Imagem</label>
                    <input id="stockEntity-image" name="stockEntity-image" className="form-control" onInput={updateImage} type="file" accept="image/*" />
                </div>
                <button className="btn btn-primary mt-3" onClick={update} disabled={loadingUpdate}>
                    Atualizar
                </button>
            </div>
        </div>
    )
}