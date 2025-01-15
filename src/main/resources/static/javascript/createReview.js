let rating = 0; // To store the current rating

// Function to set the rating based on the clicked star value
function rate(value) {
    rating = value;
    const stars = document.querySelectorAll(".restaurant-review-rating-group .star");
    stars.forEach((star) => {
        const starValue = parseInt(star.getAttribute("data-value"));
        if (starValue <= rating) {
            // Set filled star
            star.innerHTML = '<i class="fa-solid fa-star" style="color:#ffa500"></i>';
        } else {
            // Set empty star
            star.innerHTML = '<i class="fa-regular fa-star"></i>';
        }
    });
}

// Function to clear ratings
function clearRating() {
    rating = 0;
    const stars = document.querySelectorAll(".restaurant-review-rating-group .star");
    stars.forEach((star) => {
        // Reset all stars to empty
        star.innerHTML = '<i class="fa-regular fa-star"></i>';
    });
}

// Add event listener for each star
const stars = document.querySelectorAll(".restaurant-review-rating-group .star");
stars.forEach((star) => {
    star.addEventListener("click", function() {
        const value = parseInt(star.getAttribute("data-value"));
        rate(value);
    });
});

// Add event listener for clear button
const clearButton = document.querySelector(".clear-rating");
clearButton.addEventListener("click", clearRating);

// Add event listener for the save button
document.querySelector(".restaurant-review-save-button").addEventListener("click", function(event) {
    event.preventDefault(); // Prevent form submission

    // Fetch the review text
    const reviewText = document.getElementById("writtenReview").value;

    // Get current date
    const currentDate = new Date().toISOString().split('T')[0]; // Format YYYY-MM-DD

    // Get query parameters
    const params = new URLSearchParams(window.location.search);
    const orderId = params.get("oid");
    const restaurantId = params.get("rid");
    const userId = params.get("uid");

    // Construct review object
    const reviewData = {
        userId: userId,
        restaurantId: restaurantId,
        orderId: orderId,
        rating: rating,
        reviewText: reviewText,
        date: currentDate,
        reviewResponse: null
    };

    // Make a POST request to save the review
    fetch('/add-review', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reviewData)
    })
    .then(response => {
        if (response.ok) {
            alert('Review submitted successfully!');
            window.location.href = '/order-history'; // Redirect to reviews page or another relevant page
        } else {
            alert('Failed to submit the review. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while submitting your review.');
    });
});
