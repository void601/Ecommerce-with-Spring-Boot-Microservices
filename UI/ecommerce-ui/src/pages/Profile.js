import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          navigate("/login");
          return;
        }

        // Fetch user details
        const userResponse = await axios.get("http://localhost:8080/users/profile", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setUser(userResponse.data);

        // Fetch user orders
        const ordersResponse = await axios.get(`http://localhost:8080/orders/${userResponse.data.id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const productsWithUniqueKey = ordersResponse.data.map((order) => ({
          ...order,
          items: order.items.map((item) => ({
            ...item,
            uniqueKey: `${item.id.productId}--${item.id.title}--${item.id.category}`,
          })),
        }));
        setOrders(productsWithUniqueKey);
      } catch (error) {
        console.error("Failed to fetch profile or orders:", error);
      }
    };

    fetchProfile();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  if (!user) {
    return (
      <div style={styles.loadingContainer}>
        <div style={styles.neonLoader}></div>
        <p>Loading profile...</p>
      </div>
    );
  }

  return (
    <div style={styles.background}>
      <div style={styles.profileContainer}>
        {/* Profile Card */}
        <div style={styles.profileCard}>
          <h2 style={styles.neonText}>Welcome, {user.username}!</h2>
          <p><strong>Email:</strong> {user.email}</p>
        </div>

        {/* Order History */}
        <h3 style={styles.neonHeading}>Order History</h3>
        {orders.length === 0 ? (
          <p style={styles.noOrders}>No orders found.</p>
        ) : (
          <div style={styles.ordersGrid}>
            {orders.map((order) => (
              <div key={order.id} style={styles.orderCard}>
                <p><strong>Order ID:</strong> {order.id}</p>
                <p><strong>Total Amount:</strong> ${order.totalAmount.toFixed(2)}</p>
                <p><strong>Payment Status:</strong> {order.paymentStatus}</p>
                <div style={styles.itemsList}>
                  <h4>Items:</h4>
                  {order.items.map((item) => (
                    <div key={item.uniqueKey} style={styles.itemCard}>
                      <Link to={`/products/${item.uniqueKey}`} style={styles.neonLink}>
                        {item.id.title}
                      </Link>
                      {/* <p><strong>Description:</strong> {item.description}</p> */}
                      <p><strong>Quantity:</strong> {item.quantity}</p>
                      <p><strong>Price:</strong> ${item.price.toFixed(2)}</p>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Logout Button */}
        <button onClick={handleLogout} style={styles.logoutButton}>
          Logout
        </button>
      </div>
    </div>
  );
};

export default Profile;

// Futuristic Styles
const styles = {
  background: {
    minHeight: "100vh",
    background: "linear-gradient(-45deg, #1a1a2e, #16213e, #0f3460, #1a1a2e)",
    backgroundSize: "400% 400%",
    animation: "gradientBackground 15s ease infinite",
    padding: "20px",
    fontFamily: "'Orbitron', sans-serif",
    color: "#e0e0e0",
  },
  profileContainer: {
    maxWidth: "1200px",
    margin: "auto",
    padding: "20px",
  },
  profileCard: {
    padding: "20px",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    marginBottom: "30px",
    textAlign: "center",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.3)",
  },
  neonText: {
    color: "#00ffff",
    textShadow: "0 0 10px #00ffff, 0 0 20px #00ffff",
  },
  neonHeading: {
    color: "#00ff88",
    textShadow: "0 0 10px #00ff88, 0 0 20px #00ff88",
    marginBottom: "20px",
  },
  ordersGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
    gap: "20px",
  },
  orderCard: {
    padding: "15px",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    transition: "transform 0.3s, box-shadow 0.3s",
  },
  orderCardHover: {
    transform: "scale(1.05)",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.5)",
  },
  itemsList: {
    marginTop: "10px",
  },
  itemCard: {
    padding: "10px",
    marginBottom: "10px",
    borderLeft: "3px solid #00ffff",
  },
  neonLink: {
    color: "#00ffff",
    textDecoration: "none",
    textShadow: "0 0 5px #00ffff",
  },
  noOrders: {
    textAlign: "center",
    fontSize: "16px",
    color: "#777",
  },
  logoutButton: {
    padding: "12px 20px",
    backgroundColor: "#dc3545",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    marginTop: "20px",
    fontSize: "16px",
    display: "block",
    width: "100%",
    transition: "background 0.3s",
  },
  logoutButtonHover: {
    backgroundColor: "#b02a37",
  },
  loadingContainer: {
    textAlign: "center",
    marginTop: "50px",
    fontSize: "18px",
    color: "#555",
  },
  neonLoader: {
    display: "inline-block",
    width: "50px",
    height: "50px",
    border: "5px solid #00ffff",
    borderTop: "5px solid transparent",
    borderRadius: "50%",
    animation: "spin 1s linear infinite",
  },
};

// Add this to your global styles (e.g., index.css)
// @keyframes gradientBackground {
//   0% { background-position: 0% 50%; }
//   50% { background-position: 100% 50%; }
//   100% { background-position: 0% 50%; }
// }

// @keyframes spin {
//   0% { transform: rotate(0deg); }
//   100% { transform: rotate(360deg); }
// }