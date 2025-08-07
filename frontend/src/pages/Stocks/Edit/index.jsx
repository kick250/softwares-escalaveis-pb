import {useNavigate, useParams} from "react-router-dom";
import {editStore} from "@/store/Stocks/editStore.js";
import {useEffect, useState} from "react";

export default function Edit() {
    const navigate = useNavigate();
    const { id } = useParams();
    const { stockParams, loading, loadingUpdate, loadStock, updateName, updateStock } = editStore();
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        (async () => {
            await loadStock(id);
        })();
    }, []);

    const update = async () => {
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
                            successMessage && <span className="text-success">{successMessage}</span>
                        }
                        <div className="d-flex flex-column bg-light w-25 py-3 px-5 rounded-3 shadow">
                            <div>
                                <label htmlFor="stock-name" className="form-label">Nome</label>
                                <input id="stock-name" name="stock-name" className="form-control" value={stockParams.name} placeholder="Nome"
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