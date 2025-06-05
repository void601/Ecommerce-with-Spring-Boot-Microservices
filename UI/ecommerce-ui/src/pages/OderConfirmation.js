import React from "react";
import { useNavigate } from "react-router-dom";

const OrderConfirmation = () => {
  const navigate = useNavigate();

  return (
    <div style={styles.background}>
      <div style={styles.confirmationContainer}>
        <h2 style={styles.neonText}>Order Confirmed!</h2>
        <p style={styles.message}>Thank you for your purchase.</p>
        <p style={styles.subMessage}>Your order has been placed successfully.</p>
        <button
          onClick={() => navigate("/products")}
          style={styles.continueButton}
          onMouseOver={(e) => (e.target.style.backgroundColor = "#00cc88")}
          onMouseOut={(e) => (e.target.style.backgroundColor = "#00ff99")}
        >
          Continue Shopping
        </button>
      </div>
    </div>
  );
};

export default OrderConfirmation;

// Futuristic Styles
const styles = {
  background: {
    minHeight: "100vh",
    background: "linear-gradient(-45deg, #1a1a2e, #16213e, #0f3460, #1a1a2e)",
    backgroundSize: "400% 400%",
    animation: "gradientBackground 15s ease infinite",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    fontFamily: "'Orbitron', sans-serif",
    color: "#e0e0e0",
  },
  confirmationContainer: {
    padding: "30px",
    textAlign: "center",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.3)",
    maxWidth: "400px",
  },
  neonText: {
    color: "#00ff99",
    textShadow: "0 0 10px #00ff99, 0 0 20px #00ff99",
    fontSize: "32px",
    marginBottom: "15px",
  },
  message: {
    fontSize: "18px",
    color: "#e0e0e0",
    marginBottom: "10px",
  },
  subMessage: {
    fontSize: "16px",
    color: "#bbb",
    marginBottom: "20px",
  },
  continueButton: {
    padding: "12px 24px",
    backgroundColor: "#00ff99",
    color: "#1a1a2e",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontSize: "16px",
    transition: "background 0.3s, transform 0.3s",
    boxShadow: "0 0 10px rgba(0, 255, 153, 0.5)",
  },
};

// Add this to your global styles (e.g., index.css)
// @keyframes gradientBackground {
//   0% { background-position: 0% 50%; }
//   50% { background-position: 100% 50%; }
//   100% { background-position: 0% 50%; }
// }