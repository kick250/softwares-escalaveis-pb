import {Link} from "react-router-dom";
import './index.css';

export default function DefaultHeader() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <Link className="ms-2 navbar-brand" to="/">
                <img src="/vite.svg" alt="Logo" width="30" height="30"/>
            </Link>
            <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
                <div className="navbar-nav">
                    <Link className="nav-item nav-link" to="/">Home</Link>
                    <Link className="nav-item nav-link" to="/stocks">Estoques</Link>
                    <Link className="nav-item nav-link" to="/products">Produtos</Link>
                    <Link className="nav-item nav-link" to="/about">Sobre</Link>
                </div>
            </div>
        </nav>
    )
}