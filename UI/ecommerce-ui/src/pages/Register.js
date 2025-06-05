import React, { useState } from "react";
import { TextField, Button, Container, Typography, Paper } from "@mui/material";
import axios from "axios";
import registerBg from "../assets/images/register.jpg"; // Background image

const Register = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({
    email: "",
    password: "",
  });

  const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email) ? "" : "Invalid email format";
  };

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password)
      ? ""
      : "Password must be at least 8 characters, include uppercase, lowercase, number & special character";
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    // Validate fields in real-time
    if (name === "email") {
      setErrors((prev) => ({ ...prev, email: validateEmail(value) }));
    }
    if (name === "password") {
      setErrors((prev) => ({ ...prev, password: validatePassword(value) }));
    }
  };

  const resetForm=()=>{
    setFormData({
        email: '',
        password: '',
        username: '',
      });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Final validation check before submitting
    const emailError = validateEmail(formData.email);
    const passwordError = validatePassword(formData.password);

    setErrors({ email: emailError, password: passwordError });

    if (emailError || passwordError) {
      alert("Please fix the errors before submitting");
      resetForm();
      return;
    }

    try {
      const response = await axios.post("http://localhost:8080/users/register", formData);
      console.log("Registration Successful:", response.data);
      resetForm();
      alert("Registration Successful!");
    } catch (error) {
      console.error("Registration failed:", error);
      resetForm();
      alert("Registration failed. Please try again.");
    }
  };

  return (
    <div style={styles.background}>
      <Container component="main" maxWidth="xs">
        <Paper elevation={10} style={styles.paper}>
          <Typography variant="h5" gutterBottom style={styles.heading}>
            Create Account
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Username"
              variant="outlined"
              name="username"
              value={formData.username}
              onChange={handleChange}
              margin="normal"
              required
              InputProps={{ style: { color: "#fff" } }}
              sx={styles.inputField}
            />
            <TextField
              fullWidth
              label="Email"
              variant="outlined"
              name="email"
              value={formData.email}
              onChange={handleChange}
              margin="normal"
              required
              error={!!errors.email}
              helperText={errors.email}
              InputProps={{ style: { color: "#fff" } }}
              sx={styles.inputField}
            />
            <TextField
              fullWidth
              label="Password"
              variant="outlined"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              margin="normal"
              required
              error={!!errors.password}
              helperText={errors.password}
              InputProps={{ style: { color: "#fff" } }}
              sx={styles.inputField}
            />
            <Button type="submit" variant="contained" color="primary" fullWidth sx={styles.button}>
              Register
            </Button>
          </form>
        </Paper>
      </Container>
    </div>
  );
};

// Styles
const styles = {
  background: {
    minHeight: "100vh",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    backgroundImage: `url(${registerBg})`,
    backgroundSize: "cover",
    backgroundPosition: "center",
  },
  paper: {
    padding: "40px",
    borderRadius: "12px",
    textAlign: "center",
    background: "rgba(0, 0, 0, 0.6)",
    backdropFilter: "blur(10px)",
    border: "1px solid rgba(255, 255, 255, 0.2)",
    color: "#fff",
  },
  heading: {
    fontWeight: "bold",
    color: "#00e5ff",
    textShadow: "0px 0px 8px #00e5ff",
  },
  inputField: {
    "& .MuiOutlinedInput-root": {
      "& fieldset": {
        borderColor: "rgba(255, 255, 255, 0.5)",
      },
      "&:hover fieldset": {
        borderColor: "#00e5ff",
      },
      "&.Mui-focused fieldset": {
        borderColor: "#00e5ff",
      },
    },
    "& .MuiInputLabel-root": {
      color: "rgba(255, 255, 255, 0.7)",
    },
    "& .MuiInputLabel-root.Mui-focused": {
      color: "#00e5ff",
    },
    "& .MuiOutlinedInput-input": {
      color: "#fff",
    },
  },
  button: {
    marginTop: "20px",
    padding: "12px",
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
};

export default Register;
