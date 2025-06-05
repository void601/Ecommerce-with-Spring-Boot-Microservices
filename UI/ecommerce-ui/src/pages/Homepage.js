import React from "react";

const Homepage = () => {
  return (
    <div style={styles.background}>
      {/* Header */}
      <header style={styles.header}>
        <h1 style={styles.neonText}>Welcome to <span style={styles.highlight}>ECart</span></h1>
        <p style={styles.subHeader}>Your Futuristic eCommerce Experience</p>
      </header>

      {/* Main Content */}
      <main style={styles.main}>
        {/* About Section */}
        <section style={styles.section}>
          <h2 style={styles.neonHeading}>About ECart</h2>
          <p style={styles.text}>
            ECart is a cutting-edge eCommerce platform designed to provide a seamless and futuristic shopping experience. 
            Built with modern technologies, it offers features like real-time product browsing, secure payments, and personalized 
            order tracking.
          </p>
        </section>

        {/* Features Section */}
        <section style={styles.section}>
          <h2 style={styles.neonHeading}>Features</h2>
          <div style={styles.featuresGrid}>
            <div style={styles.featureCard}>
              <h3 style={styles.featureTitle}>Real-Time Product Browsing</h3>
              <p style={styles.featureText}>
                Explore a wide range of products with real-time updates and dynamic filtering.
              </p>
            </div>
            <div style={styles.featureCard}>
              <h3 style={styles.featureTitle}>Secure Payments</h3>
              <p style={styles.featureText}>
                Enjoy secure and hassle-free payments with integrated payment gateways.
              </p>
            </div>
            <div style={styles.featureCard}>
              <h3 style={styles.featureTitle}>Personalized Order Tracking</h3>
              <p style={styles.featureText}>
                Track your orders in real-time with personalized updates and notifications.
              </p>
            </div>
          </div>
        </section>

        {/* How It Works Section */}
        <section style={styles.section}>
          <h2 style={styles.neonHeading}>How It Works</h2>
          <div style={styles.stepsGrid}>
            <div style={styles.stepCard}>
              <h3 style={styles.stepTitle}>1. Browse Products</h3>
              <p style={styles.stepText}>
                Explore our catalog of products with advanced search and filtering options.
              </p>
            </div>
            <div style={styles.stepCard}>
              <h3 style={styles.stepTitle}>2. Add to Cart</h3>
              <p style={styles.stepText}>
                Add your favorite products to the cart and proceed to checkout.
              </p>
            </div>
            <div style={styles.stepCard}>
              <h3 style={styles.stepTitle}>3. Place Order</h3>
              <p style={styles.stepText}>
                Complete your purchase with secure payment options and confirm your order.
              </p>
            </div>
          </div>
        </section>

        {/* Technologies Section */}
        <section style={styles.section}>
          <h2 style={styles.neonHeading}>Technologies Used</h2>
          <div style={styles.techGrid}>
            <div style={styles.techCard}>
              <h3 style={styles.techTitle}>React</h3>
              <p style={styles.techText}>
                A powerful JavaScript library for building user interfaces.
              </p>
            </div>
            <div style={styles.techCard}>
              <h3 style={styles.techTitle}>Spring Boot</h3>
              <p style={styles.techText}>
                A robust backend framework for building scalable microservices.
              </p>
            </div>
            <div style={styles.techCard}>
              <h3 style={styles.techTitle}>Postgres</h3>
              <p style={styles.techText}>
                A SQL database for storing and managing product and order data.
              </p>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Homepage;

// Futuristic Styles (No Green Effects)
const styles = {
  background: {
    minHeight: "100vh",
    background: "linear-gradient(-45deg, #1a1a2e, #16213e, #0f3460, #1a1a2e)",
    backgroundSize: "400% 400%",
    animation: "gradientBackground 15s ease infinite",
    color: "#e0e0e0",
    fontFamily: "'Orbitron', sans-serif",
    padding: "20px",
  },
  header: {
    textAlign: "center",
    padding: "50px 20px",
  },
  neonText: {
    fontSize: "48px",
    color: "#00ffff",
    textShadow: "0 0 10px #00ffff, 0 0 20px #00ffff",
  },
  highlight: {
    color: "#00ffff", // Purple instead of green
    textShadow: "0 0 10px #00ffff, 0 0 20px #00ffff",
  },
  subHeader: {
    fontSize: "20px",
    color: "#bbb",
  },
  main: {
    maxWidth: "1200px",
    margin: "auto",
    padding: "20px",
  },
  section: {
    marginBottom: "50px",
  },
  neonHeading: {
    fontSize: "32px",
    color: "#00ffff", // Cyan instead of green
    textShadow: "0 0 10px #00ffff, 0 0 20px #00ffff",
    marginBottom: "20px",
  },
  text: {
    fontSize: "18px",
    color: "#e0e0e0",
    lineHeight: "1.6",
  },
  featuresGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
    gap: "20px",
  },
  featureCard: {
    padding: "20px",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    transition: "transform 0.3s, box-shadow 0.3s",
  },
  featureCardHover: {
    transform: "scale(1.05)",
    boxShadow: "0 0 20px rgba(0, 255, 255, 0.5)",
  },
  featureTitle: {
    fontSize: "24px",
    color: "#00ffff", // Cyan instead of green
    marginBottom: "10px",
  },
  featureText: {
    fontSize: "16px",
    color: "#bbb",
  },
  stepsGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
    gap: "20px",
  },
  stepCard: {
    padding: "20px",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    transition: "transform 0.3s, box-shadow 0.3s",
  },
  stepTitle: {
    fontSize: "24px",
    color: "#00ffff", // Cyan instead of green
    marginBottom: "10px",
  },
  stepText: {
    fontSize: "16px",
    color: "#bbb",
  },
  techGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
    gap: "20px",
  },
  techCard: {
    padding: "20px",
    borderRadius: "12px",
    background: "rgba(255, 255, 255, 0.1)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    transition: "transform 0.3s, box-shadow 0.3s",
  },
  techTitle: {
    fontSize: "24px",
    color: "#00ffff", // Cyan instead of green
    marginBottom: "10px",
  },
  techText: {
    fontSize: "16px",
    color: "#bbb",
  },
  footer: {
    textAlign: "center",
    padding: "20px",
    borderTop: "1px solid rgba(255, 255, 255, 0.2)",
    marginTop: "50px",
  },
  footerText: {
    fontSize: "16px",
    color: "#bbb",
  },
};

// Add this to your global styles (e.g., index.css)
// @keyframes gradientBackground {
//   0% { background-position: 0% 50%; }
//   50% { background-position: 100% 50%; }
//   100% { background-position: 0% 50%; }
// }