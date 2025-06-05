import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  getCart,
  removeFromCart,
  clearCart,
  updateCartItemQuantity,
} from "../services/cartService";
import {
  Container,
  Typography,
  Card,
  CardContent,
  CardActions,
  Button,
  Grid,
  IconButton,
  Collapse,
  Box,
  TextField,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";

const Cart = () => {
  const [cart, setCart] = useState([]);
  const [expandedDescriptions, setExpandedDescriptions] = useState({}); // State for expanded descriptions
  const navigate = useNavigate();

  // Load cart items from local storage
  useEffect(() => {
    setCart(getCart());
    console.log("cart: ", getCart());
  }, []);

  // Calculate total price
  const totalPrice = cart.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  // Handle Remove from Cart
  const handleRemoveFromCart = (productId) => {
    removeFromCart(productId);
    setCart(getCart()); // Update cart state
  };

  // Handle Clear Cart
  const handleClearCart = () => {
    clearCart();
    setCart([]); // Clear cart state
  };

  // Handle Expand/Collapse Description
  const handleToggleDescription = (uniqueKey) => {
    setExpandedDescriptions((prev) => ({
      ...prev,
      [uniqueKey]: !prev[uniqueKey], // Toggle expanded state
    }));
  };

  // Handle Quantity Change
  const handleQuantityChange = (productId, newQuantity) => {
    if (newQuantity < 1) return; // Ensure quantity is at least 1
    updateCartItemQuantity(productId, newQuantity);
    setCart(getCart()); // Update cart state
  };

  return (
    <div style={styles.pageBackground}>
      <Container maxWidth="md" style={styles.container}>
        <Typography variant="h3" align="center" gutterBottom style={styles.title}>
          Your Cart
        </Typography>
        {cart.length === 0 ? (
          <Typography variant="body1" align="center" style={styles.noResults}>
            Your cart is empty.
          </Typography>
        ) : (
          <>
            <Grid container spacing={3} style={{ marginBottom: "20px" }}>
              {cart.map((item) => {
                const uniqueKey = `${item.id.productId}--${item.id.title}--${item.id.category}`; // Generate unique key
                return (
                  <Grid item xs={12} sm={6} key={uniqueKey}>
                    <Card style={styles.card}>
                      <CardContent style={styles.cardContent}>
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

                        {/* Item Description */}
                        <Collapse in={expandedDescriptions[uniqueKey]} collapsedSize={50}>
                          <Typography variant="body2" style={styles.productDescription}>
                            {item.description}
                          </Typography>
                        </Collapse>

                        {/* Show More/Show Less Button with Text */}
                        <Box
                          display="flex"
                          alignItems="center"
                          onClick={() => handleToggleDescription(uniqueKey)}
                          sx={{ cursor: "pointer", marginTop: "5px" }}
                        >
                          <Typography variant="body2" color="primary">
                            {expandedDescriptions[uniqueKey] ? "Show Less" : "Show More"}
                          </Typography>
                          <IconButton size="small">
                            {expandedDescriptions[uniqueKey] ? (
                              <ExpandLessIcon fontSize="small" />
                            ) : (
                              <ExpandMoreIcon fontSize="small" />
                            )}
                          </IconButton>
                        </Box>

                        {/* Item Price */}
                        <Typography variant="h6" style={styles.productPrice}>
                          ${item.price}
                        </Typography>

                        {/* Item Quantity */}
                        <Box display="flex" alignItems="center" marginTop="10px">
                          <Button
                            variant="outlined"
                            onClick={() => handleQuantityChange(item.id.productId, item.quantity - 1)}
                            style={styles.quantityButton}
                          >
                            -
                          </Button>
                          <TextField
                            type="number"
                            value={item.quantity}
                            onChange={(e) =>
                              handleQuantityChange(item.id.productId, parseInt(e.target.value))
                            }
                            inputProps={{ min: 1 }}
                            style={styles.quantityInput}
                            InputProps={{
                              style: { color: "#fff" }, // Set text color to white
                            }}
                          />
                          <Button
                            variant="outlined"
                            onClick={() => handleQuantityChange(item.id.productId, item.quantity + 1)}
                            style={styles.quantityButton}
                          >
                            +
                          </Button>
                        </Box>
                      </CardContent>
                      <CardActions style={styles.cardActions}>
                        <Button
                          variant="contained"
                          style={styles.removeButton}
                          onClick={() => handleRemoveFromCart(item.id.productId)}
                        >
                          Remove
                        </Button>
                      </CardActions>
                    </Card>
                  </Grid>
                );
              })}
            </Grid>
            <div style={{ textAlign: "right", marginTop: "20px" }}>
              <Typography variant="h5" gutterBottom style={styles.totalPrice}>
                Total: ${totalPrice.toFixed(2)}
              </Typography>
              <Button
                variant="contained"
                style={styles.clearButton}
                onClick={handleClearCart}
                sx={{ marginRight: "10px" }}
              >
                Clear Cart
              </Button>
              <Button
                variant="contained"
                style={styles.checkoutButton}
                onClick={() => navigate("/checkout")}
              >
                Checkout
              </Button>
            </div>
          </>
        )}
      </Container>
    </div>
  );
};

export default Cart;

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
  },
  title: {
    color: "#00e5ff",
    textShadow: "0px 0px 15px rgba(0, 229, 255, 0.8)",
    fontWeight: "bold",
    letterSpacing: "2px",
    marginBottom: "30px",
    textTransform: "uppercase",
  },
  noResults: {
    color: "#fff",
  },
  card: {
    height: "100%",
    display: "flex",
    flexDirection: "column",
    background: "rgba(0, 0, 0, 0.6)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    borderRadius: "12px",
    transition: "transform 0.3s ease, box-shadow 0.3s ease",
    "&:hover": {
      transform: "scale(1.02)",
      boxShadow: "0px 0px 20px rgba(0, 229, 255, 0.5)",
    },
  },
  cardContent: {
    flexGrow: 1,
    padding: "20px",
  },
  productTitle: {
    color: "#00e5ff",
    fontWeight: "bold",
  },
  productDescription: {
    color: "rgba(255, 255, 255, 0.7)",
  },
  productPrice: {
    color: "#00e5ff",
    marginTop: "10px",
    fontWeight: "bold",
  },
  productImage: {
    width: "80px",
    height: "80px",
    objectFit: "cover",
    borderRadius: "8px",
  },
  quantityInput: {
    width: "60px",
    margin: "0 10px",
    "& input": {
      textAlign: "center",
      background: "rgba(255, 255, 255, 0.1)",
      borderRadius: "6px",
    },
  },
  quantityButton: {
    color: "#00e5ff",
    borderColor: "#00e5ff",
    minWidth: "30px",
    padding: "5px",
    "&:hover": {
      borderColor: "#00bcd4",
      color: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  removeButton: {
    backgroundColor: "#ff1744",
    color: "#fff",
    "&:hover": {
      backgroundColor: "#d50000",
      boxShadow: "0px 0px 15px #ff1744",
    },
  },
  clearButton: {
    backgroundColor: "#ff1744",
    color: "#fff",
    "&:hover": {
      backgroundColor: "#d50000",
      boxShadow: "0px 0px 15px #ff1744",
    },
  },
  checkoutButton: {
    backgroundColor: "#00e5ff",
    color: "#000",
    "&:hover": {
      backgroundColor: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  totalPrice: {
    color: "#00e5ff",
    fontWeight: "bold",
  },
};