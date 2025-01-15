
function getFoodItems() {
    fetch('/get-food-items') 
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); 
        })
        .then(foodItems => {
            console.log('Fetched food items:', foodItems); 
           
            displayFoodItems(foodItems);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function displayFoodItems(foodItems) {
    const foodList = document.getElementById('foodList'); 

   
    foodList.innerHTML = '';

	foodItems.forEach(item => {
	    const foodLink = document.createElement('a');
		foodLink.href = `/single-food-page?itemId=${item.itemId}`;
	    foodLink.style.textDecoration = 'none';          // Optional: Remove underline
	    foodLink.style.color = 'inherit';                // Optional: Inherit color for consistency

	    const foodCard = document.createElement('div');
	    foodCard.classList.add('food-card');

	    const foodImage = document.createElement('img');
	    foodImage.src = `http://localhost:8080/images_RestaurantM/${item.images}` || 'default-image.jpg';
	    foodImage.alt = item.itemName;

	    const details = document.createElement('div');
	    details.classList.add('details');

	    const itemName = document.createElement('h3');
	    itemName.textContent = item.itemName;
	    itemName.style.marginTop = "5px";

	    const restaurantName = document.createElement('h4');
	    restaurantName.textContent = item.restaurantName;
	    restaurantName.style.marginTop = "5px";

	    const rating = document.createElement('p');
	    rating.innerHTML = `<i class="fa-solid fa-star" style="color:#1E712A"></i>${item.rating}`;
	    rating.style.marginTop = "5px";

	    const itemPrice = document.createElement('p');
	    itemPrice.textContent = `₹${item.price}`;
	    itemPrice.style.marginTop = "5px";

	    const itemStock = document.createElement('p');
	    itemStock.textContent = `Available: ${item.stock}`;
	    itemStock.style.marginTop = "5px";

	    foodCard.appendChild(foodImage);
	    details.appendChild(itemName);
	    details.appendChild(restaurantName);
	    details.appendChild(rating);
	    details.appendChild(itemPrice);
	    details.appendChild(itemStock);
	    foodCard.appendChild(details);

	    // Wrap the foodCard inside the anchor link
	    foodLink.appendChild(foodCard);
	    foodList.appendChild(foodLink);  // Append the link to the foodList
	});

}

document.addEventListener('DOMContentLoaded', function() {
    getFoodItems();
});
