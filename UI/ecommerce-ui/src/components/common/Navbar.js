import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";

const Navbar = () => {
  const navigate = useNavigate();
  const isLoggedIn = localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <AppBar position="static" sx={styles.navbar}>
      <Toolbar>
        {/* App Title */}
        <Typography variant="h6" component="div" sx={styles.logo}>
          <Link to="/" style={styles.link}>
            E-Commerce App
          </Link>
        </Typography>

        {/* Conditional Rendering for Logged-In Users */}
        {isLoggedIn ? (
          <>
            <Button sx={styles.navButton} component={Link} to="/products">
              Products
            </Button>
            <Button sx={styles.navButton} component={Link} to="/cart">
              <ShoppingCartIcon sx={{ marginRight: 1 }} />
              Cart
            </Button>
            <Button sx={styles.navButton} component={Link} to="/profile">
              Profile
            </Button>
            <Button sx={styles.logoutButton} onClick={handleLogout}>
              Logout
            </Button>
          </>
        ) : (
          <>
            <Button sx={styles.navButton} component={Link} to="/login">
              Login
            </Button>
            <Button sx={styles.navButton} component={Link} to="/register">
              Register
            </Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
};

// Styles
const styles = {
  navbar: {
    background: "rgba(12, 12, 12, 0.84)", // Black faded background
    backdropFilter: "blur(6px)", // Subtle frosted glass effect
    borderBottom: "1px solid rgba(0, 229, 255, 0.3)", // Soft neon bottom border
    boxShadow: "0px 0px 10px rgba(0, 229, 255, 0.2)", // Slight neon glow
  },
  logo: {
    flexGrow: 1,
    fontWeight: "bold",
    color: "#00e5ff",
    textShadow: "0px 0px 6pxrgb(195, 242, 242)", // Soft neon glow
  },
  link: {
    color: "inherit",
    textDecoration: "none",
  },
  navButton: {
    color: "#00e5ff",
    fontWeight: "bold",
    transition: "all 0.3s ease",
    "&:hover": {
      color: "#00bcd4",
      textShadow: "0px 0px 6pxrgb(245, 245, 245)",
    },
  },
  logoutButton: {
    color: "#ff1744",
    fontWeight: "bold",
    transition: "all 0.3s ease",
    "&:hover": {
      color: "#ff5252",
      textShadow: "0px 0px 6px #ff1744",
    },
  },

};

export default Navbar;
