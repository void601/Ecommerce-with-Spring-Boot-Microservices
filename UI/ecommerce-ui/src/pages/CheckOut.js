import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCart, clearCart } from "../services/cartService";
import axios from "axios";
import {
  Container,
  Typography,
  Card,
  CardContent,
  TextField,
  Button,
  Grid,
  Box,
} from "@mui/material";

const Checkout = () => {
  const [cart, setCart] = useState([]);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    setCart(getCart());
  }, []);

  // Calculate total price
  const totalPrice = cart.reduce((total, item) => total + item.price * item.quantity, 0);

  // Form state
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    city: "",
    state: "",
    zip: "",
    cardNumber: "",
    expiryDate: "",
    cvv: "",
  });

  // Handle form input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Handle order submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const order = {
        userId,
        orderItems: cart,
        totalAmount: totalPrice,
        shippingDetails: {
          name: formData.name,
          address: formData.address,
          city: formData.city,
          state: formData.state,
          zip: formData.zip,
        },
        paymentDetails: {
          cardNumber: formData.cardNumber,
          expiryDate: formData.expiryDate,
          cvv: formData.cvv,
        },
      };

      console.log("OrderRequest:", order);
      await axios.post("http://localhost:8080/orders", order, {
        headers: { Authorization: `Bearer ${token}` },
      });

      clearCart();
      navigate("/order-confirmation");
    } catch (error) {
      console.error("Failed to place order:", error);
      alert("Order failed. Try again.");
    }
  };

  return (
    <div style={styles.pageBackground}>
      <Container maxWidth="md" style={styles.container}>
        {/* <Typography variant="h3" align="center" gutterBottom style={styles.title}>
          Checkout
        </Typography> */}
        <Grid container spacing={3}>
          {/* Order Summary */}
          <Grid item xs={12} md={4}>
            <Typography variant="h5" style={styles.sectionTitle}>
              Order Summary
            </Typography>
            {cart.map((item) => {
              const totalItemPrice = item.price * item.quantity; // Calculate total price for the item
              return (
                <Card key={item.uniqueId} style={styles.card}>
                  <CardContent>
                    {/* Item Image and Title */}
                    <Box
                      display="flex"
                      justifyContent="space-between"
                      alignItems="flex-start"
                    >
                      {/* Item Title */}
                      <Typography variant="h6" style={styles.productTitle}>
                        {item.id.title}
                      </Typography>

                      {/* Item Image */}
                      <img
                        src={item.image}
                        alt={item.id.title}
                        style={styles.productImage}
                      />
                    </Box>

                    {/* Item Details */}
                    <Typography variant="body2" style={styles.productDescription}>
                      Quantity: {item.quantity}
                    </Typography>
                    <Typography variant="body2" style={styles.productDescription}>
                      Price: ${item.price.toFixed(2)}
                    </Typography>
                    <Typography variant="body2" style={styles.productDescription}>
                      Total: ${totalItemPrice.toFixed(2)}
                    </Typography>
                  </CardContent>
                </Card>
              );
            })}
            <Typography variant="h5" style={styles.totalPrice}>
              Grand Total: ${totalPrice.toFixed(2)}
            </Typography>
          </Grid>

          {/* Shipping & Payment Form */}
          <Grid item xs={12} md={8}>
            <Typography variant="h5" style={styles.sectionTitle}>
              Shipping & Payment
            </Typography>
            <form onSubmit={handleSubmit}>
              <TextField
                label="Full Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" }, // Set text color to white
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" }, // Set label color to light gray
                }}
              />
              <TextField
                label="Address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="City"
                name="city"
                value={formData.city}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="State"
                name="state"
                value={formData.state}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="ZIP Code"
                name="zip"
                value={formData.zip}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="Card Number"
                name="cardNumber"
                value={formData.cardNumber}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                type="number"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="Expiry Date (MM/YY)"
                name="expiryDate"
                value={formData.expiryDate}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />
              <TextField
                label="CVV"
                name="cvv"
                value={formData.cvv}
                onChange={handleChange}
                fullWidth
                required
                margin="normal"
                type="password"
                style={styles.inputField}
                InputProps={{
                  style: { color: "#fff" },
                }}
                InputLabelProps={{
                  style: { color: "rgba(255, 255, 255, 0.7)" },
                }}
              />

              <Button
                type="submit"
                variant="contained"
                style={styles.placeOrderButton}
                fullWidth
                sx={{ marginTop: "20px" }}
              >
                Place Order
              </Button>
            </form>
          </Grid>
        </Grid>
      </Container>
    </div>
  );
};

export default Checkout;

// Styling
const styles = {
  pageBackground: {
    minHeight: "100vh",
    width: "100%",
    background: "linear-gradient(135deg, #0f0c29, #302b63, #24243e)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "20px",
  },
  container: {
    padding: "40px",
    maxWidth: "1200px",
    position: "relative",
    minHeight: "100vh",
  },
  title: {
    color: "#00e5ff",
    textShadow: "0px 0px 15px rgba(0, 229, 255, 0.8)",
    fontWeight: "bold",
    letterSpacing: "2px",
    marginBottom: "30px",
    textTransform: "uppercase",
  },
  sectionTitle: {
    color: "#00e5ff",
    fontWeight: "bold",
    marginBottom: "20px",
  },
  card: {
    background: "rgba(0, 0, 0, 0.6)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    borderRadius: "12px",
    marginBottom: "10px",
    transition: "transform 0.3s ease, box-shadow 0.3s ease",
    "&:hover": {
      transform: "scale(1.02)",
      boxShadow: "0px 0px 20px rgba(0, 229, 255, 0.5)",
    },
  },
  productTitle: {
    color: "#00e5ff",
    fontWeight: "bold",
  },
  productDescription: {
    color: "rgba(255, 255, 255, 0.7)",
  },
  productImage: {
    width: "80px",
    height: "80px",
    objectFit: "cover",
    borderRadius: "8px",
  },
  totalPrice: {
    color: "#00e5ff",
    fontWeight: "bold",
    marginTop: "20px",
  },
  inputField: {
    background: "rgba(255, 255, 255, 0.1)",
    borderRadius: "6px",
    "& input": {
      color: "#fff",
    },
    "& label": {
      color: "rgba(255, 255, 255, 0.7)",
    },
  },
  placeOrderButton: {
    backgroundColor: "#00e5ff",
    color: "#000",
    fontWeight: "bold",
    "&:hover": {
      backgroundColor: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  footer: {
    position: "absolute",
    bottom: "20px",
    left: "50%",
    transform: "translateX(-50%)",
    width: "100%",
    textAlign: "center",
  },
  footerText: {
    color: "rgba(255, 255, 255, 0.7)",
  },
};