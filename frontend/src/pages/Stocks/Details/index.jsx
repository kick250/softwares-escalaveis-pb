import {Link, useParams} from "react-router-dom";
import {detailsStore} from "@/store/Stocks/detailsStore.js";
import {useEffect} from "react";
import Stock from "@/pages/Stocks/Details/Stock.jsx";

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
                    <Stock />
            }
        </div>
    );
}