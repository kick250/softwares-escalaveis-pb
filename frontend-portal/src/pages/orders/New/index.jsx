import {newOrderStore} from "@/stores/newOrderStore.js";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

export default function New() {
    const navigate = useNavigate();
    const { selectedStockId, loadCart, getStocks, getStockProducts, getCartProducts,
        loadStocks, loadProducts, getProductQuantity, setProductQuantity, updateCart, clearCart, sendOrder
    } = newOrderStore();
    const [addProductMode, setAddProductMode] = useState(true);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        (async () => {
            await loadStocks();
            await loadCart();
            setIsLoading(false);
        })();
    }, []);

    const selectStockHandler = (event) => {
        clearCart();
        const stockId = event.target.value;
        loadProducts(stockId);
    };

    const getProductImageUrl = (product) => {
        return import.meta.env.VITE_API_URL + product.imagePath;
    }

    const productQuantityHandler = (event, product) => {
        const quantity = parseInt(event.target.value);

        if (isNaN(quantity) || quantity < 0) return setProductQuantity(product, 0);
        setProductQuantity(product, quantity);
    };

    const addToCartHandler = () => {
        setAddProductMode(false);
        updateCart();
    }

    const sendOrderHandler = async () => {
        await sendOrder();
        clearCart();
        setAddProductMode(false);
        alert("Pedido enviado com sucesso!");
        navigate("/");
    }

    if (isLoading) {
        return (
            <div>
                <div className="row mx-2 w-100 justify-content-between align-items-center">
                    <div className="col-4">
                        <button className="btn btn-secondary" onClick={() => navigate(-1)}>Voltar</button>
                    </div>
                    <div className="col-4 text-center">
                        <h1>Novo pedido</h1>
                    </div>
                    <div className="col-4">
                    </div>
                    <div className="col-12 mt-3 text-center">
                        <div className="spinner-border text-primary" role="status">
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div>
            <div className="row mx-2 w-100 justify-content-between align-items-center">
                <div className="col-4">
                    <button className="btn btn-secondary" onClick={() => navigate(-1)}>Voltar</button>
                </div>
                <div className="col-4 text-center">
                    <h1>Novo pedido</h1>
                </div>
                <div className="col-4">
                </div>
            </div>
            <div className="row justify-content-center">
                <div className="col-2 text-center">
                    <label className="form-label">Estoque</label>
                    <select className="form-select" onChange={selectStockHandler} value={selectedStockId}>
                        <option value="-1" disabled={true}>Selecione um estoque</option>
                        {getStocks().map(stock => (
                            <option key={stock.id} value={stock.id}>{stock.name}</option>
                        ))}
                    </select>
                </div>
            </div>
            { !isLoading && addProductMode &&
                <div className="row m-5">
                    <div className="col-12 text-center mb-3">
                        <h2>Produtos</h2>
                    </div>
                    {getStockProducts().length === 0 && (
                        <div className="col-12 text-center">
                            <p>Nenhum produto disponível</p>
                        </div>
                    )}
                    {getStockProducts().map(product => (
                        <div key={product.id} className="col-3 text-center mb-3">
                            <div className="card">
                                <div className="card-body">
                                    <img className="card-img-top" src={getProductImageUrl(product)} alt={product.name}/>
                                    <h5 className="card-title">{product.name}</h5>
                                    <p className="card-text mb-0">R$ {product.price.toFixed(2)}</p>
                                    <div className="card-body p-0 d-flex flex-column">
                                        <div>
                                            <span className="fw-bold">Quantidade disponivel:</span>
                                            <span> {product.quantityAvailable}</span>
                                        </div>
                                        <div>
                                            <label className="fw-bold">Quantidade solicitada:</label>
                                            <input type="number" className="form-control text-center" defaultValue={getProductQuantity(product.id)} min="1"
                                                   onInput={(e) => {productQuantityHandler(e, product);}}
                                                   onKeyDown={(e) => {
                                                       if (e.key === '-' || e.key.toLowerCase() === 'e' || e.key === '+') e.preventDefault();
                                                   }} />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                    {getCartProducts().length > 0 && (
                        <div className="col-12 text-center mt-3">
                            <button className="btn btn-primary" onClick={addToCartHandler}>Adicionar ao carrinho</button>
                        </div>
                    )}
                </div>
            }
            {
                !isLoading && !addProductMode &&
                    <div className="row m-5">
                        <div className="col-12 text-center mb-3">
                            <h2>Carrinho</h2>
                        </div>
                        {getCartProducts().map(product => (
                            <div key={product.id} className="col-3 text-center mb-3">
                                <div className="card">
                                    <div className="card-body">
                                        <img className="card-img-top" src={getProductImageUrl(product)} alt={product.name}/>
                                        <h5 className="card-title">{product.name}</h5>
                                        <p className="card-text mb-0">R$ {product.price.toFixed(2)}</p>
                                        <p className="card-text mb-0">Quantidade solicitada: {product.quantityRequested}</p>
                                        <p className="card-text mb-0">Subtotal: R$ {(product.price * product.quantityRequested).toFixed(2)}</p>
                                    </div>
                                </div>
                            </div>
                        ))}
                        {getCartProducts().length > 0 && (
                            <div className="col-12 text-center mt-3">
                                <button className="btn btn-success" onClick={sendOrderHandler}>Enviar pedido</button>
                            </div>
                        )}
                    </div>
            }
        </div>
    );
}