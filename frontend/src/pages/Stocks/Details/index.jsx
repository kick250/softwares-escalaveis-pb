import {Link, useParams} from "react-router-dom";
import {detailsStore} from "@/store/Stocks/detailsStore.js";
import {useEffect} from "react";

export default function Details() {
    const { id } = useParams();
    const { stock, loading, loadStock } = detailsStore();

    useEffect(() => {
        (async () => {
            await loadStock(id);
        })();
    }, [])

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Detalhes do Estoque</h1>
            {
                loading ?
                    <div className="spinner-border text-primary mt-5"></div>
                :
                    <div className="w-25 text-center bg-light rounded p-3">
                        <div>
                            <label className="mb-2">Identicador</label>
                            <input value={`#${id}`} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Nome</label>
                            <input value={stock.name} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="d-flex flex-column mt-3">
                            <Link to={`/stocks/${id}/edit`} className="btn btn-primary w-100 mt-2">
                                Editar
                            </Link>
                            <Link to={`/stocks/${id}/delete`} className="btn btn-danger w-100 mt-2">
                                Excluir
                            </Link>
                        </div>
                    </div>
            }
        </div>
    );
}