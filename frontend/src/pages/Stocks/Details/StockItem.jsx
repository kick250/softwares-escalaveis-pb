import {Link, useParams} from "react-router-dom";

export default function StockItem(props) {
    const { id } = useParams();
    const { item } = props;
    return (
        <div className="col-12 px-5">
            <div className="mb-2 row border p-3 rounded mx-0">
                <div className="col-1">
                    <p className="mb-2 w-100 text-center">Identificador</p>
                    <input value={`#${item.id}`} className="form-control text-center" disabled={true} />
                </div>
                <div className="col-3">
                    <p className="mb-2 w-100 text-center">Nome</p>
                    <input value={`${item.name}`} className="form-control text-center" disabled={true} />
                </div>
                <div className="col-3">
                    <p className="mb-2 w-100 text-center">Preço neste estoque</p>
                    <input value={`R$ ${item.price}`} className="form-control text-center" disabled={true} />
                </div>
                <div className="col-3">
                    <p className="mb-2 w-100 text-center">Quantidade disponivel</p>
                    <input value={`${item.quantity}`} className="form-control text-center" disabled={true} />
                </div>
                <div className="col-2 d-flex align-items-end">
                    <Link to={`/stocks/${id}/items/${item.id}`} className="btn btn-primary w-100 mt-2">
                        Ver detalhes
                    </Link>
                </div>
            </div>
        </div>
    );
};