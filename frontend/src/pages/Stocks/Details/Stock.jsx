import {Link, useParams} from "react-router-dom";
import {detailsStore} from "@/store/Stocks/detailsStore.js";
import StockItem from "@/pages/Stocks/Details/StockItem.jsx";

export default function Stock() {
    const { id } = useParams();
    const { stock } = detailsStore();

    return (
        <>
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
            <h2 className="mt-5">Itens neste estoque</h2>
            <div className="row w-100 mt-3 mx-0">
                {stock.stockItems && stock.stockItems.length > 0 ? (
                    stock.stockItems.map((item) => (
                        <StockItem item={item} key={item.id}/>
                    ))
                ) : (
                    <p className="h3 text-center">Nenhum item encontrado neste estoque.</p>
                )}
            </div>
            <div className="w-100 mt-2 px-5 d-flex justify-content-end">
                <Link to={`/stocks/${id}/new_item`} className="btn btn-success px-5 mb-3">
                    Adicionar Item
                </Link>
            </div>
        </>
    );
}