import './App.css';
import Register from './pages/Register';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Profile from './pages/Profile';
import Navbar from './components/common/Navbar';
import ProductList from './pages/ProductList';
import ProductDetails from './pages/ProductDetails';
import Cart from './pages/Cart';
import Checkout from './pages/CheckOut';
import OrderConfirmation from './pages/OderConfirmation';
import Homepage from './pages/Homepage';
function App() {
  return (
      <Router>
        <Navbar/>
        <Routes>
        <Route path="/" element={<Homepage/>}/>
          <Route path="/home" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/profile" element={<Profile/>}/>
          <Route path="/products" element={<ProductList/>} />
          <Route path="/products/:productId" element={<ProductDetails />} /> 
          <Route path="/cart" element={<Cart />} /> {/* Cart route */}
          <Route path="/checkout" element={<Checkout />} /> {/* Checkout route */}
          <Route path="/order-confirmation" element={<OrderConfirmation />} />
        </Routes>
      </Router>
  );
}

export default App;
