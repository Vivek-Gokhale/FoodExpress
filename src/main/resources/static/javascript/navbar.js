document.addEventListener('DOMContentLoaded', function () {
  const isLoggedIn = localStorage.getItem('isLoggedInCustomer');
  const profileDropdown = document.getElementById('profile-dropdown');
  const loginDropdown = document.getElementById('login-dropdown');
  const profileButton = document.querySelector('.profile-dropdown-btn');

  if (isLoggedIn) {
    let username = localStorage.getItem('usernameCustomer') || 'User'; // Default username if not found
    username = username.charAt(0).toUpperCase() + username.slice(1);
    profileButton.innerHTML = `<i class="fa-solid fa-user"></i> ${username} <i class="fa-solid fa-caret-down"></i>`;
    loginDropdown.style.display = 'none';  // Hide login dropdown
    profileDropdown.style.display = 'block';  // Show profile dropdown
  } else {
    loginDropdown.style.display = 'block';  // Show login dropdown
    profileDropdown.style.display = 'none';  // Hide profile dropdown
  }

  const cartBtn = document.getElementById("cartBtn");  // Fixed: Use getElementById

  // Add click event to navigate to the 'add-to-cart' page
  if (cartBtn) {
    cartBtn.addEventListener('click', function () {
      window.location.href = '/add-to-cart';  // Change URL to your 'add-to-cart' page route
    });
  }
});
