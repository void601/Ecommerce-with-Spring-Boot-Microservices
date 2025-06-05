const CART_KEY = "cart";

// Get the current cart from localStorage
const getCart = () => {
  const cart = localStorage.getItem(CART_KEY);
  return cart ? JSON.parse(cart) : [];
};

// Add a product to the cart
const addToCart = (product) => {
  const cart = getCart();
  const existingProduct = cart.find(
    (item) => item.id.productId === product.id.productId
  );

  if (existingProduct) {
    // If the product already exists in the cart, increase its quantity
    existingProduct.quantity += product.quantity;
  } else {
    // If the product is not in the cart, add it with a quantity of 1
    cart.push({ ...product, quantity: product.quantity });
  }

  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

// Remove a product from the cart
const removeFromCart = (productId) => {
  const cart = getCart();
  const updatedCart = cart.filter(
    (item) => item.id.productId !== productId
  );
  localStorage.setItem(CART_KEY, JSON.stringify(updatedCart));
};

// Update the quantity of a product in the cart
const updateCartItemQuantity = (productId, newQuantity) => {
  const cart = getCart();
  const updatedCart = cart.map((item) => {
    if (item.id.productId === productId) {
      return { ...item, quantity: newQuantity }; // Update the quantity
    }
    return item;
  });
  localStorage.setItem(CART_KEY, JSON.stringify(updatedCart));
};

// Clear the cart
const clearCart = () => {
  localStorage.removeItem(CART_KEY);
};

export { getCart, addToCart, removeFromCart, updateCartItemQuantity, clearCart };