import {useEffect} from "react";
import {ordersStore} from "@/stores/ordersStore.js";
import {Link} from "react-router-dom";

export default function Home() {
    const { getOrders, loadOrders } = ordersStore();

    useEffect(() => {
        loadOrders();
    }, [])

    return (
        <div>
            <div>
                <div className="row mx-5 justify-content-between align-items-center">
                    <div className="col-4"></div>
                    <div className="col-4 text-center">
                        <h1>Pedidos</h1>
                    </div>
                    <div className="col-4 text-end">
                        <Link to={"/orders/new"} className="btn btn-primary float-end">Novo Pedido</Link>
                    </div>
                </div>
                <div className="px-5">
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Identificador</th>
                            <th scope="col">Estoque</th>
                            <th scope="col">Status</th>
                            <th scope="col">Solicitante</th>
                            <th scope="col">Data de criação</th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        {getOrders().map((order) => (
                            <tr key={order.id}>
                                <th scope="row">#{order.id}</th>
                                <td>{order.stockName}</td>
                                <td>{order.translatedStatus}</td>
                                <td>{order.ownerName}</td>
                                <td>{new Date(order.createdAt).toLocaleDateString('pt-BR')}</td>
                                <td>
                                    <Link to={`/orders/${order.id}`}>Ver</Link>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}