import { BrowserRouter, Routes, Route } from 'react-router-dom';
import {useEffect} from "react";
import {authStore} from "@/stores/authStore.js";
import Login from "@/pages/Login/index.jsx";
import Home from '@/pages/Home';
import NewOrder from "@/pages/orders/New";
import OrderDetails from "@/pages/orders/Details";
import NotFound from "@/pages/NotFound";

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
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/orders/new" element={<NewOrder />} />
                <Route path="/orders/:id" element={<OrderDetails />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}
