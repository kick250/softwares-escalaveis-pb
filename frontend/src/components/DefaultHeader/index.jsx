import {Link} from "react-router-dom";
import './index.css';
import {authStore} from "@/store/authStore.js";

export default function DefaultHeader() {
    const { logout } = authStore();

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <Link className="ms-2 navbar-brand" to="/">
                <img src="/vite.svg" alt="Logo" width="30" height="30"/>
            </Link>
            <div className="collapse navbar-collapse justify-content-between" id="navbarNavAltMarkup">
                <div className="navbar-nav">
                    <Link className="nav-item nav-link" to="/">Home</Link>
                    <Link className="nav-item nav-link" to="/stockEntities">Estoques</Link>
                    <Link className="nav-item nav-link" to="/productEntities">Produtos</Link>
                    <Link className="nav-item nav-link" to="/about">Sobre</Link>
                </div>
                <button className="btn btn-secondary me-5" onClick={logout}>Logout</button>
            </div>
        </nav>
    )
}