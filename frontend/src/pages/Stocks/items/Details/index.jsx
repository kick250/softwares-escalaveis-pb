import {Link, useParams} from "react-router-dom";
import {detailsStore} from "@/store/Stocks/Items/detailsStore.js";
import {useEffect, useState} from "react";

export default function Details() {
    const { id, stockId } = useParams();
    const { stockItem, stock, loadStockItem, loadStock } = detailsStore();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        (
            async () => {
                await loadStockItem(id);
                await loadStock(stockId);
                setLoading(false);
            }
        )()
    },[])

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Detalhes do item</h1>
            {
                loading ?
                    <div className="spinner-border text-primary mt-5"></div>
                    :
                    <div className="w-25 text-center bg-light rounded p-3">
                        <div>
                            <label className="mb-2">Imagem</label>
                            <img src={stockItem.imageUrl} alt={stockItem.name} className="img-fluid rounded mb-3"/>
                        </div>
                        <div>
                            <label className="mb-2">Identicador do item</label>
                            <input value={`#${id}`} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Nome produto</label>
                            <input value={stockItem.name} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Descrição do produto</label>
                            <textarea value={stockItem.description} className="form-control" rows={3} disabled={true} />
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Preço do item</label>
                            <input value={`R$ ${stockItem.price}`} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Quantidade disponível</label>
                            <input value={stockItem.quantity} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Estoque</label>
                            <input value={stock.name} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="d-flex flex-column mt-3">
                            <Link to={`/stocks/${stockId}/items/${id}/edit`} className="btn btn-primary w-100 mt-2">
                                Editar
                            </Link>
                            <Link to={`/stocks/${stockId}/items/${id}/delete`} className="btn btn-danger w-100 mt-2">
                                Excluir
                            </Link>
                        </div>
                    </div>
            }
        </div>
    );
};