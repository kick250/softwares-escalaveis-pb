import {homeStore} from "@/store/Products/homeStore.js";
import {useEffect} from "react";
import {Link} from "react-router-dom";

export default function Home() {
    const { products, loadProducts } = homeStore();

    useEffect(() => {
        (
           async () => {
                await loadProducts();
           }
        )()
    })

    return (
        <div className="m-3">
            <div className="d-flex justify-content-between align-items-center">
                <h1>Produtos</h1>
                <Link to={"/products/new"} className="btn btn-success mt-2" style={{width: "250px"}}>
                    Novo produto
                </Link>
            </div>
            <div className="mt-3 row">
                {
                    products.map((product) => (
                        <div className="col-3 px-3" key={product.id}>
                            <div className="card">
                                <div className="card-body">
                                    <h5 className="card-title text-center mb-3"><span className="text-muted">#{product.id}</span> {product.name}</h5>
                                    <div className="d-flex flex-column align-items-center">
                                        <Link to={`/products/${product.id}`} className="btn btn-primary w-100">
                                            Ver detalhes
                                        </Link>
                                        <Link to={`/products/${product.id}/edit`} className="btn btn-secondary w-100 mt-2">
                                            Editar
                                        </Link>
                                        <Link to={`/products/${product.id}/delete`} className="btn btn-danger w-100 mt-2">
                                            Excluir
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
}