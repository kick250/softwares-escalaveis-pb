import {useNavigate, useParams} from "react-router-dom";
import {editStore} from "@/store/Stocks/editStore.js";
import {useEffect, useState} from "react";

export default function Edit() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { stockParams, loading, loadingUpdate, loadStock, updateName, getErrorMessage, updateStock } = editStore();
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        (async () => {
            await loadStock(id);
        })();
    }, []);

    const checkFields = () => {
        const message = getErrorMessage();
        if (message) {
            setErrorMessage(message);
            return false;
        }
        return true;
    };

    const update = async () => {
        if (!checkFields()) return;

        const successMessage = await updateStock();
        setSuccessMessage(successMessage);
        setTimeout(() => navigate(`/stocks/${id}`), 1500);
    };

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Editar estoque</h1>
            {
                loading ?
                    <div className="spinner-border text-primary mt-5"></div>
                :
                    <>
                        {
                            errorMessage && <span className="text-danger">{errorMessage}</span>
                        }
                        {
                            successMessage && <span className="text-success">{successMessage}</span>
                        }
                        <div className="d-flex flex-column bg-light w-25 py-3 px-5 rounded-3 shadow">
                            <div>
                                <label htmlFor="stockEntity-name" className="form-label">Nome</label>
                                <input id="stockEntity-name" name="stockEntity-name" className="form-control" value={stockParams.name} placeholder="Nome"
                                       onInput={({target}) => updateName(target.value)}
                                />
                            </div>
                            <button className="btn btn-primary mt-3" onClick={update} disabled={loadingUpdate}>
                                Criar
                            </button>
                        </div>
                    </>
            }
        </div>
    );
}