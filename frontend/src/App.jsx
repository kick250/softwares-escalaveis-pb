import {BrowserRouter, Routes, Route, useNavigate} from 'react-router-dom';
import StockHome from './pages/Stocks/Home';
import About from './pages/About';
import DefaultHeader from "@/components/DefaultHeader";
import NotFound from "@/pages/NotFound";
import NewStock from "@/pages/Stocks/New";
import DetailsStock from "@/pages/Stocks/Details";
import EditStock from "@/pages/Stocks/Edit";
import DeleteStock from "@/pages/Stocks/Delete";
import ProductsHome from "@/pages/Products/Home";
import NewProduct from "@/pages/Products/New";
import DetailsProduct from "@/pages/Products/Details";
import EditProduct from "@/pages/Products/Edit";
import DeleteProduct from "@/pages/Products/Delete";
import NewItem from "@/pages/Stocks/NewItem";
import DetailsStockItem from "@/pages/Stocks/items/Details";
import EditStockItem from "@/pages/Stocks/items/Edit";
import DeleteStockItem from "@/pages/Stocks/items/Delete";
import Login from "@/pages/Login";
import {authStore} from "@/store/authStore.js";
import {useEffect} from "react";

export default function App() {
    const { isAuthenticated, loadSession } = authStore();

    useEffect(() => {
        loadSession();
    }, []);


    if (!isAuthenticated()) {
        return <Login />;
    }

    return (
        <BrowserRouter>
            <DefaultHeader />
            <Routes>
                <Route path="/" element={<StockHome />} />
                <Route path="/about" element={<About />} />
                <Route path="/stockEntities" element={<StockHome />} />
                <Route path="/stockEntities/new" element={<NewStock />} />
                <Route path="/stockEntities/:id" element={<DetailsStock />} />
                <Route path="/stockEntities/:id/edit" element={<EditStock />} />
                <Route path="/stockEntities/:id/delete" element={<DeleteStock />} />
                <Route path="/stockEntities/:id/new_item" element={<NewItem />} />
                <Route path="/stockEntities/:stockId/items/:id" element={<DetailsStockItem />} />
                <Route path="/stockEntities/:stockId/items/:id/edit" element={<EditStockItem />} />
                <Route path="/stockEntities/:stockId/items/:id/delete" element={<DeleteStockItem />} />
                <Route path="/productEntities" element={<ProductsHome />} />
                <Route path="/productEntities/new" element={<NewProduct />} />
                <Route path="/productEntities/:id" element={<DetailsProduct />} />
                <Route path="/productEntities/:id/edit" element={<EditProduct />} />
                <Route path="/productEntities/:id/delete" element={<DeleteProduct />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}
