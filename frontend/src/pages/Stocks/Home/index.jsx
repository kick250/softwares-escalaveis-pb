import { homeStore } from '@/store/Stocks/homeStore.js';
import {useEffect} from "react";
import {Link} from "react-router-dom";

export default function Home() {
    const { stocks, loadStocks } = homeStore();

    useEffect(() => {
        (async () => {
            await loadStocks();
        })()
    }, [])

    return (
        <div className="m-3">
            <div className="d-flex justify-content-between align-items-center">
                <h1>Estoques</h1>
                <Link to={"/stocks/new"} className="btn btn-success mt-2" style={{width: "250px"}}>
                    Novo estoque
                </Link>
            </div>
            <div className="row mt-3">
                {stocks.map((stock) => (
                    <div className="col-3 px-3" key={stock.id}>
                        <div className="card mb-3">
                            <div className="card-body">
                                <h5 className="card-title">{stock.name}</h5>
                                <h6 className="card-subtitle my-2 text-muted">Identificador: #{stock.id}</h6>
                                <p className="card-text">Itens cadastrados: {stock.length}</p>
                                <div className="d-flex flex-column align-items-center">
                                    <Link to={`/stocks/${stock.id}`} className="btn btn-primary w-100">
                                        Ver detalhes
                                    </Link>
                                    <Link to={`/stocks/${stock.id}/edit`} className="btn btn-secondary w-100 mt-2">
                                        Editar
                                    </Link>
                                    <Link to={`/stocks/${stock.id}/delete`} className="btn btn-danger w-100 mt-2">
                                        Excluir
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
