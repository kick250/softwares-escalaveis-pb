import { BrowserRouter, Routes, Route } from 'react-router-dom';
import StockHome from './pages/Stocks/Home';
import About from './pages/About';
import DefaultHeader from "@/components/DefaultHeader";
import NotFound from "@/pages/NotFound";
import NewStock from "@/pages/Stocks/New";
import DetailsStock from "@/pages/Stocks/Details";
import EditStock from "@/pages/Stocks/Edit";
import DeleteStock from "@/pages/Stocks/Delete";
import ProductsHome from "@/pages/Products/Home";

export default function App() {
    return (
        <BrowserRouter>
            <DefaultHeader />
            <Routes>
                <Route path="/" element={<StockHome />} />
                <Route path="/about" element={<About />} />
                <Route path="/stocks" element={<StockHome />} />
                <Route path="/stocks/new" element={<NewStock />} />
                <Route path="/stocks/:id" element={<DetailsStock />} />
                <Route path="/stocks/:id/edit" element={<EditStock />} />
                <Route path="/stocks/:id/delete" element={<DeleteStock />} />
                <Route path="/products" element={<ProductsHome />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}
