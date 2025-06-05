// Import the React library to define and use React components
import React from 'react';

// Define a functional component named `Input`
// It accepts props: type, placeholder, value, onChange, and name
const Input = ({ type, placeholder, value, onChange, name }) => {
  // Return the JSX (JavaScript XML) to render the input element
  return (
    // Render an `<input>` element with the following attributes:
    <input
      // Set the type of input (e.g., text, email, password)
      type={type}
      // Set the placeholder text displayed when the input is empty
      placeholder={placeholder}
      // Bind the input's value to the `value` prop
      value={value}
      // Call the `onChange` function whenever the input value changes
      onChange={onChange}
      // Set the name of the input field (useful for form handling)
      name={name}
      // Apply inline CSS styles to the input element
      style={{
        // Add a 10px margin around the input
        margin: '10px',
        // Add 8px padding inside the input
        padding: '8px',
        // Set the width of the input to 200px
        width: '200px',
        // Round the corners of the input with a 4px radius
        borderRadius: '4px',
        // Add a 1px solid border with a light gray color
        border: '1px solid #ccc',
      }}
    />
  );
};

// Export the `Input` component as the default export of this module
// This makes it available for use in other parts of the application
export default Input;