import {Link, useParams} from "react-router-dom";
import {detailsStore} from "@/store/Products/detailsStore.js";
import {useEffect} from "react";

export default function Details() {
    const { id } = useParams();
    const { loading, product, loadProduct } = detailsStore();

    useEffect(() => {
        (
            async () => {
                await loadProduct(id);
            }
        )()
    }, [])

    return (
        <div className="d-flex flex-column align-items-center">
            <h1 className="mt-3">Detalhes do produto</h1>
            {
                loading ?
                    <div className="spinner-border text-primary mt-5"></div>
                    :
                    <div className="w-25 text-center bg-light rounded p-3">
                        <div>
                            <label className="mb-2">Imagem</label>
                            <img src={product.imageUrl} alt={product.name} className="img-fluid rounded mb-3"/>
                        </div>
                        <div>
                            <label className="mb-2">Identicador</label>
                            <input value={`#${id}`} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Nome</label>
                            <input value={product.name} className="form-control text-center" disabled={true}/>
                        </div>
                        <div className="mt-3">
                            <label className="mb-2">Descrição</label>
                            <textarea value={product.description} className="form-control" rows={3} disabled={true} />
                        </div>
                        <div className="d-flex flex-column mt-3">
                            <Link to={`/products/${id}/edit`} className="btn btn-primary w-100 mt-2">
                                Editar
                            </Link>
                            <Link to={`/products/${id}/delete`} className="btn btn-danger w-100 mt-2">
                                Excluir
                            </Link>
                        </div>
                    </div>
            }
        </div>
    );
}