import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { addToCart } from "../services/cartService";
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Container,
  CircularProgress,
  Backdrop,
  Modal,
  Box,
  TextField,
} from "@mui/material";

const ProductDetails = () => {
  const { productId } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showQuantityPopup, setShowQuantityPopup] = useState(false); // State for quantity popup
  const [showSuccessPopup, setShowSuccessPopup] = useState(false); // State for success popup
  const [quantity, setQuantity] = useState(1); // State for quantity input
  const [quantityError, setQuantityError] = useState(""); // State for quantity error message
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  // Fetch product details
  useEffect(() => {
    const fetchProductDetails = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/products/api/${productId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log("Response: ", response.data);
        setProduct(response.data);
      } catch (error) {
        console.error("Failed to fetch product details:", error);
        alert("Failed to fetch product details. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchProductDetails();
  }, [productId, token]);

  // Handle Add to Cart
  const handleAddToCart = () => {
    setShowQuantityPopup(true); // Show the quantity popup
  };

  // Handle Confirm Add to Cart
  const handleConfirmAddToCart = () => {
    if (quantity < 1) {
      setQuantityError("Quantity must be at least 1."); // Set error message
      return;
    }

    if (product) {
      addToCart({ ...product, quantity }); // Add product with quantity to cart
      setShowQuantityPopup(false); // Close the quantity popup
      setShowSuccessPopup(true); // Show the success popup
      setTimeout(() => setShowSuccessPopup(false), 3000); // Hide the success popup after 3 seconds
    }
  };

  if (loading) {
    return (
      <Backdrop open={loading} style={styles.fullScreenBackdrop}>
        <div style={styles.loadingContainer}>
          <CircularProgress size={80} thickness={4} style={styles.loader} />
          <Typography variant="h6" style={styles.loadingText}>
            Loading Product Details...
          </Typography>
        </div>
      </Backdrop>
    );
  }

  if (!product) {
    return (
      <Container style={styles.noProductContainer}>
        <Typography variant="h6" style={styles.noProductText}>
          Product not found.
        </Typography>
      </Container>
    );
  }

  return (
    <div style={styles.pageBackground}>
      <Container maxWidth="md" style={styles.container}>
        <Card style={styles.card}>
          <CardContent style={styles.cardContent}>
            {/* Product Title */}
            <Typography variant="h4" gutterBottom style={styles.productTitle}>
              {product.id.title}
            </Typography>

            {/* Product Image */}
            <img
              src={product.image}
              alt={product.id.title}
              style={styles.productImage}
            />

            {/* Product Description */}
            <Typography variant="body1" style={styles.productDescription}>
              {product.description}
            </Typography>

            {/* Product Category */}
            <Typography variant="body2" style={styles.productCategory}>
              Category: {product.id.category}
            </Typography>

            {/* Product Price */}
            <Typography variant="h5" style={styles.productPrice}>
              ${product.price}
            </Typography>
          </CardContent>

          {/* Action Buttons */}
          <CardActions style={styles.cardActions}>
            <Button
              variant="contained"
              style={styles.addToCartButton}
              onClick={handleAddToCart}
            >
              Add to Cart
            </Button>
            <Button
              variant="outlined"
              style={styles.backButton}
              onClick={() => navigate(-1)}
            >
              Back
            </Button>
          </CardActions>
        </Card>
      </Container>

      {/* Quantity Popup */}
      <Modal
        open={showQuantityPopup}
        onClose={() => setShowQuantityPopup(false)}
        style={styles.modal}
      >
        <Box style={styles.popup}>
          <Typography variant="h6" style={styles.popupText}>
            How many "{product.id.title}" would you like to add to cart?
          </Typography>
          <TextField
            type="number"
            value={quantity}
            onChange={(e) => {
              setQuantity(Math.max(1, parseInt(e.target.value)));
              setQuantityError(""); // Clear error message when user types
            }}
            inputProps={{
              min: 1, // Minimum value
              style: { color: "#fff", textAlign: "center" }, // White text color
            }}
            style={styles.quantityInput}
            error={!!quantityError} // Show error state
            helperText={quantityError} // Display error message
          />
          <Button
            variant="contained"
            style={styles.confirmButton}
            onClick={handleConfirmAddToCart}
          >
            Confirm
          </Button>
          <Button
            variant="outlined"
            style={styles.cancelButton}
            onClick={() => setShowQuantityPopup(false)}
          >
            Cancel
          </Button>
        </Box>
      </Modal>

      {/* Success Popup */}
      <Modal
        open={showSuccessPopup}
        onClose={() => setShowSuccessPopup(false)}
        style={styles.modal}
      >
        <Box style={styles.popup}>
          <Typography variant="h6" style={styles.popupText}>
            {product.id.title} (Quantity: {quantity}) added to cart!
          </Typography>
        </Box>
      </Modal>
    </div>
  );
};

export default ProductDetails;

// Styling
const styles = {
  fullScreenBackdrop: {
    zIndex: 1300,
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100vh",
    background: "#000",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  loadingContainer: {
    textAlign: "center",
    padding: "20px",
    borderRadius: "12px",
    background: "rgba(0, 0, 0, 0.8)",
    boxShadow: "0px 0px 15px rgba(0, 229, 255, 0.8)",
  },
  loader: {
    color: "#00e5ff",
    animation: "pulse 1.5s infinite",
  },
  loadingText: {
    marginTop: "20px",
    color: "#00e5ff",
    fontWeight: "bold",
  },
  noProductContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
  },
  noProductText: {
    color: "#fff",
  },
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
  },
  card: {
    background: "rgba(0, 0, 0, 0.6)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    borderRadius: "12px",
    padding: "20px",
    transition: "transform 0.3s ease, box-shadow 0.3s ease",
    "&:hover": {
      transform: "scale(1.02)",
      boxShadow: "0px 0px 20px rgba(0, 229, 255, 0.5)",
    },
  },
  cardContent: {
    textAlign: "center",
  },
  productTitle: {
    color: "#00e5ff",
    fontWeight: "bold",
    textShadow: "0px 0px 15px rgba(0, 229, 255, 0.8)",
    marginBottom: "20px",
    fontSize: "1.8rem",
  },
  productImage: {
    width: "100%",
    maxHeight: "250px",
    objectFit: "cover",
    borderRadius: "12px",
    marginBottom: "20px",
  },
  productDescription: {
    color: "rgba(255, 255, 255, 0.7)",
    marginBottom: "20px",
    fontSize: "0.9rem",
  },
  productCategory: {
    color: "rgba(255, 255, 255, 0.7)",
    marginBottom: "20px",
    fontSize: "0.9rem",
  },
  productPrice: {
    color: "#00e5ff",
    fontWeight: "bold",
    marginBottom: "20px",
    fontSize: "1.5rem",
  },
  cardActions: {
    display: "flex",
    justifyContent: "center",
    gap: "20px",
  },
  addToCartButton: {
    backgroundColor: "#00e5ff",
    color: "#000",
    fontWeight: "bold",
    borderRadius: "6px",
    transition: "all 0.3s ease",
    "&:hover": {
      backgroundColor: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  backButton: {
    borderColor: "#00e5ff",
    color: "#00e5ff",
    fontWeight: "bold",
    borderRadius: "6px",
    transition: "all 0.3s ease",
    "&:hover": {
      borderColor: "#00bcd4",
      color: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  modal: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
  },
  popup: {
    background: "rgba(0, 0, 0, 0.8)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    borderRadius: "12px",
    padding: "20px",
    textAlign: "center",
    boxShadow: "0px 0px 20px rgba(0, 229, 255, 0.5)",
  },
  popupText: {
    color: "#00e5ff",
    fontWeight: "bold",
    marginBottom: "20px",
  },
  quantityInput: {
    marginBottom: "20px",
    width: "100%",
    background: "rgba(255, 255, 255, 0.1)",
    borderRadius: "6px",
    "& input": {
      color: "#fff", // White text color
      textAlign: "center",
      WebkitAppearance: "textfield", // Ensure arrows are visible in WebKit browsers
      MozAppearance: "textfield", // Ensure arrows are visible in Firefox
    },
    "& input[type=number]::-webkit-inner-spin-button, & input[type=number]::-webkit-outer-spin-button": {
      WebkitAppearance: "inner-spin-button", // Show arrows in WebKit browsers
    },
  },
  confirmButton: {
    backgroundColor: "#00e5ff",
    color: "#000",
    fontWeight: "bold",
    borderRadius: "6px",
    marginRight: "10px",
    "&:hover": {
      backgroundColor: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
  cancelButton: {
    borderColor: "#00e5ff",
    color: "#00e5ff",
    fontWeight: "bold",
    borderRadius: "6px",
    "&:hover": {
      borderColor: "#00bcd4",
      color: "#00bcd4",
      boxShadow: "0px 0px 15px #00e5ff",
    },
  },
};