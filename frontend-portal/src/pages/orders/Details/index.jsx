import {orderDetailStore} from "@/stores/orderDetailStore.js";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect} from "react";

export default function Details() {
    const { order, loadOrder, approveOrder, cancelOrder } = orderDetailStore();
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        loadOrder(id);
    }, []);

    const getTotal = (order) => {
        return order.items.reduce((total, item) => total + item.totalPrice, 0);
    }

    const approveOrderHandler = async () => {
        try {
            await approveOrder(id);
            alert("Pedido aprovado com sucesso!");
        } catch (error) {
            alert("Erro ao aprovar pedido: " + error.message);
        }
        loadOrder(id);
    }

    const cancelOrderHandler = async () => {
        try {
            await cancelOrder(id);
            alert("Pedido cancelar com sucesso!");
        } catch (error) {
            alert("Erro ao cancelar pedido: " + error.message);
        }
        loadOrder(id);
    }

    return (
        <div className="d-flex flex-column align-items-center">
            <div className="row w-100 justify-content-between align-items-center">
                <div className="col-4">
                    <button className="btn btn-secondary" onClick={() => navigate(-1)}>Voltar</button>
                </div>
                <div className="col-4 text-center">
                    <h1>Detalhes</h1>
                </div>
                <div className="col-4">
                    {
                        order !== undefined && order.status === "WAITING_APPROVAL" &&
                            <div>
                                <button className="btn btn-success" onClick={approveOrderHandler}>Aprovar pedido</button>
                                <button className="btn btn-danger ms-2" onClick={cancelOrderHandler}>Cancelar pedido</button>
                            </div>
                    }
                </div>
            </div>
            {
                order === undefined ?
                    <div className="spinner-border text-primary mt-3" role="status"></div>
                    :
                    <div className="card mt-3" style={{ width: '30rem' }}>
                        <div className="card-body">
                            <h5 className="card-title">Pedido #{order.id}</h5>
                            <h6 className="card-subtitle">Cliente: {order.ownerName}</h6>
                            <span className="card-text">Data do Pedido: {new Date(order.createdAt).toLocaleDateString()}</span><br/>
                            <span className="card-text">Status: {order.translatedStatus}</span>
                            <h6 className="my-0">Itens do Pedido:</h6>
                            <ul className="list-group list-group-flush">
                                {order.items.map((item) => (
                                    <li key={item.id} className="list-group-item">
                                        {item.name} - Quantidade: {item.quantity} - Preço: R$ {item.pricePerUnit.toFixed(2)}
                                    </li>
                                ))}
                            </ul>
                            <h5 className="mt-3">Total: R$ {getTotal(order).toFixed(2)}</h5>
                        </div>
                    </div>
            }

        </div>
    );
}