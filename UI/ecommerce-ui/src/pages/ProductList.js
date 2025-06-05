import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import {
  TextField,
  Grid,
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Container,
  CardMedia,
  CircularProgress,
  Backdrop,
  Collapse,
  IconButton,
  Box,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [expandedDescriptions, setExpandedDescriptions] = useState({}); // State for expanded descriptions
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  // Fetch products from backend
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true); // Show loading screen
      console.log("Token: ",token);
      try {
        const response = await axios.get(`http://localhost:8080/products/api/list`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        console.log("Response data: ", response.data);
        const productsWithUniqueKey = response.data.map((product) => ({
          ...product,
          uniqueKey: `${product.id.productId}--${product.id.title}--${product.id.category}`, // Create a unique key
        }));
        setTimeout(() => {
          setProducts(productsWithUniqueKey); // Set the state with all data (including duplicates)
          setLoading(false); // Hide loading after data fetch
        }, 100); // Simulating delay for better effect
      } catch (error) {
        console.error("Failed to fetch products:", error);
        alert("Failed to fetch products. Please try again.");
        setLoading(false);
      }
    };
    fetchProducts();
  }, [token]);

  // Log products state after it has been updated
  useEffect(() => {
    console.log("Products State (After Setting):", products);
  }, [products]);

  // Filter products based on search query
  const filteredProducts = products.filter((product) => {
    if (!product) return false; // Skip invalid products

    // Check if the fields exist and are strings
    const title = product.id.title ? product.id.title.toLowerCase() : "";
    const description = product.description ? product.description.toLowerCase() : "";
    const category = product.id.category ? product.id.category.toLowerCase() : "";

    return (
      title.includes(searchQuery.toLowerCase()) ||
      description.includes(searchQuery.toLowerCase()) ||
      category.includes(searchQuery.toLowerCase())
    );
  });

  // Handle Expand/Collapse Description
  const handleToggleDescription = (uniqueKey) => {
    setExpandedDescriptions((prev) => ({
      ...prev,
      [uniqueKey]: !prev[uniqueKey], // Toggle expanded state
    }));
  };

  return (
    <>
      {/* Full-Screen Loading Overlay */}
      {loading && (
        <Backdrop open={loading} style={styles.fullScreenBackdrop}>
          <div style={styles.loadingContainer}>
            <CircularProgress size={80} thickness={4} style={styles.loader} />
            <Typography variant="h6" style={styles.loadingText}>
              Loading Products...
            </Typography>
          </div>
        </Backdrop>
      )}

      {/* Only show content when loading is done */}
      {!loading && (
        <div style={styles.pageBackground}>
          <Container style={styles.container}>
            {/* Product List Title */}
            <Typography variant="h3" align="center" gutterBottom style={styles.title}>
              Product List
            </Typography>

            {/* Search Bar */}
            <TextField
              label="Search products..."
              variant="outlined"
              fullWidth
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={styles.searchBar}
              InputLabelProps={{ style: styles.searchLabel }}
              InputProps={{
                style: styles.searchInput,
              }}
            />

            {/* Product Grid with Search Filtering */}
            <Grid container spacing={3}>
              {filteredProducts.length > 0 ? (
                filteredProducts.map((product) => (
                  <Grid item xs={12} sm={6} md={4} key={product.uniqueKey}>
                    <Card style={styles.card}>
                      <CardMedia
                        component="img"
                        height="200"
                        image={product.image}
                        alt={product.id.title}
                        style={styles.cardImage}
                      />
                      <CardContent style={styles.cardContent}>
                        <Typography variant="h6" gutterBottom style={styles.productTitle}>
                          {product.id.title}
                        </Typography>

                        {/* Product Description with Show More/Less */}
                        <Collapse in={expandedDescriptions[product.uniqueKey]} collapsedSize={50}>
                          <Typography variant="body2" style={styles.productDescription}>
                            {product.description}
                          </Typography>
                        </Collapse>

                        {/* Show More/Show Less Button with Text */}
                        <Box
                          display="flex"
                          alignItems="center"
                          onClick={() => handleToggleDescription(product.uniqueKey)}
                          sx={{ cursor: "pointer", marginTop: "5px" }}
                        >
                          <Typography variant="body2" color="primary">
                            {expandedDescriptions[product.uniqueKey] ? "Show Less" : "Show More"}
                          </Typography>
                          <IconButton size="small">
                            {expandedDescriptions[product.uniqueKey] ? (
                              <ExpandLessIcon fontSize="small" />
                            ) : (
                              <ExpandMoreIcon fontSize="small" />
                            )}
                          </IconButton>
                        </Box>

                        <Typography variant="body2" style={styles.productDescription}>
                          {product.id.category}
                        </Typography>
                        <Typography variant="h6" style={styles.productPrice}>
                          ${product.price}
                        </Typography>
                      </CardContent>
                      <CardActions style={styles.cardActions}>
                        <Button
                          variant="contained"
                          fullWidth
                          style={styles.viewButton}
                          onClick={() => navigate(`/products/${product.uniqueKey}`)}
                        >
                          View Details
                        </Button>
                      </CardActions>
                    </Card>
                  </Grid>
                ))
              ) : (
                <Typography variant="h6" align="center" style={styles.noResults}>
                  No products found.
                </Typography>
              )}
            </Grid>
          </Container>
        </div>
      )}
    </>
  );
};

export default ProductList;

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
  searchBar: {
    marginBottom: "30px",
    background: "rgba(255, 255, 255, 0.1)",
    borderRadius: "6px",
  },
  searchLabel: {
    color: "rgba(255, 255, 255, 0.7)",
  },
  searchInput: {
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
      transform: "scale(1.05)",
      boxShadow: "0px 0px 20px rgba(0, 229, 255, 0.5)",
    },
  },
  cardImage: {
    borderTopLeftRadius: "12px",
    borderTopRightRadius: "12px",
  },
  cardContent: {
    flexGrow: 1,
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
  cardActions: {
    marginTop: "auto",
  },
  viewButton: {
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
  noResults: {
    color: "#fff",
    marginTop: "20px",
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
};