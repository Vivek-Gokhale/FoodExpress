
function getFoodItems() {
    const urlParams = new URLSearchParams(window.location.search);
    const queryItemId = urlParams.get('itemId');  // Get itemId from query parameters

    fetch('/get-food-items') 
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); 
        })
        .then(foodItems => {
            console.log('Fetched food items:', foodItems); 

            const matchedItem = foodItems.find(item => item.itemId == queryItemId);  // Match itemId
            if (matchedItem) {
				console.log(matchedItem);
                displayFoodItem(matchedItem);  // Display matched item
				fillAddress(matchedItem);
				fillReviews(matchedItem.restaurantId);
            } else {
                console.log('Item not found for itemId:', queryItemId);
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}


function displayFoodItem(foodDetails) {
	const imagePath = `/images_RestaurantM/${foodDetails.images}`;
	const container = document.querySelector('.single-food-container');

	const foodHTML = `
	    <div class="image-section">
	        <img src="${imagePath}" alt="${foodDetails.itemName}">
	    </div>
	    <div class="details-section">
	        <h1>${foodDetails.itemName}</h1>
	        <p class="restaurant-name">${foodDetails.restaurantName}</p>
			<div class="rating" style='display:flex;margin-top:10px;'>
				${"<i class='fa-solid fa-star' style='color: #ffa500'></i>".repeat(foodDetails.rating) + "<i class='fa-regular fa-star' style='color: #ffa500'></i>".repeat(5 - foodDetails.rating)}
			</div>
	        <p class="description">${foodDetails.description}</p>
	        <p class="price">₹${foodDetails.price.toFixed(2)}</p>
	        <p class="stock">Available : ${foodDetails.stock}</p>
	        <div class="productQuantityElement">
	            <p class="productProperty">Quantity</p>
	            <div class="stockElement">
	                <button class="cartDecrement">-</button>
	                <p class="productQuantity">1</p>
	                <button class="cartIncrement">+</button>
	            </div>
	        </div>
	        <div class="buttons">
	            <button class="add-to-cart">ADD <i class="fa-solid fa-cart-shopping"></i></button>
	        </div>
	    </div>
	`;

	container.innerHTML = foodHTML;

	// Quantity controls
	const quantityElement = container.querySelector('.productQuantity');
	const decrementButton = container.querySelector('.cartDecrement');
	const incrementButton = container.querySelector('.cartIncrement');
	let quantity = 1;
	const maxQuantity = foodDetails.stock;

	decrementButton.addEventListener('click', function () {
		if (quantity > 1) {
			quantity -= 1;
			quantityElement.textContent = quantity;
		}
	});

	incrementButton.addEventListener('click', function () {
		if (quantity < maxQuantity) {
			quantity += 1;
			quantityElement.textContent = quantity;
		}
	});

	// Add-to-cart button event
	const addToCartButton = container.querySelector('.add-to-cart');
	
		
	addToCartButton.addEventListener('click', function () {
		const isLoggedInCustomer = localStorage.getItem("isLoggedInCustomer");
			if(!isLoggedInCustomer)
				{
					window.location.href = "/customer-login";
					return;
				}
		const userId = localStorage.getItem('userIdCustomer'); // Assuming userId is stored in localStorage
		const itemId = foodDetails.itemId;  // Assuming itemId comes from foodDetails
		const payload = {
			userId: parseInt(userId),
			itemId: itemId,
			quantity: quantity
		};

		fetch('/add-to-cart', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(payload)
		})
		.then(response => response.json())
		.then(data => {
			alert('Item added to cart successfully!');
			console.log('Cart data:', data);
		})
		.catch(error => {
			alert('Failed to add item to cart.');
			console.error('Error:', error);
		});
	});
}



function convertTo12HourFormat(time) {
  let [hours, minutes] = time.split(':');
  hours = parseInt(hours);
  let period = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12;
  hours = hours ? hours : 12; // The hour '0' should be '12'
  minutes = minutes.padStart(2, '0'); // Ensure two digits for minutes
  return `${hours}:${minutes} ${period}`;
}

function fillAddress(addressDetails) {
  const addressContainer = document.querySelector('.address-container');
  
  // Accessing elements directly within the container
  const addressText = addressContainer.querySelector('.address-text');
  const addressLocation = addressContainer.querySelector('.address-location');

  // Fill in the text section with the provided details
  addressText.querySelector('.address').textContent = addressDetails.address;
  addressText.querySelector('.address').style.fontSize = "18px";
  addressText.querySelector('.opening-time').textContent = convertTo12HourFormat(addressDetails.startTime);
  addressText.querySelector('.closing-time').textContent = convertTo12HourFormat(addressDetails.closeTime);
  addressText.querySelector('.phone').textContent = addressDetails.phoneNo;
  addressText.querySelector('.email').textContent = addressDetails.email;

  // Fill in the location section
  const [lat, lon] = addressDetails.location.split(',');

  const mapUrl = `https://www.google.com/maps?q=${lat},${lon}&hl=es;z=14&output=embed`;
             document.getElementById('addressMap').src = mapUrl;
}


const fillReviews = async (restaurantId) => {
    try {
        const response = await fetch(`/get-restaurant-reviews/${restaurantId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch reviews');
        }
        const reviews = await response.json();


        const reviewsContainer = document.querySelector('.reviews-container');
		console.log(reviews);
		reviewsContainer.innerHTML = reviews.length > 0 
		    ? reviews.map(review => `
		        <div class="review-card">
		            <p class="reviewer-name">
		                <i class="fa-solid fa-user fa-xl" style="color: grey;"></i>&nbsp;&nbsp;
		                <b>${review.usernameString}</b>
		            </p>
					
					<div class="rating" style='display:flex;margin-top:10px;'>
							                ${"<i class='fa-solid fa-star' style='color: #ffa500'></i>".repeat(review.rating) + "<i class='fa-regular fa-star' style='color: #ffa500'></i>".repeat(5 - review.rating)}
								<p style="margin-left:10px;"><img src="/images/verified_15587409.png" alt="Verified" width="20px" height="20px;"></i></p>
								<span style="color:#8B0000;">&nbsp;&nbsp;Verified</span>
								<b style="margin-left:10px;">${new Date(review.date).toLocaleDateString('en-GB', {
															                    day: 'numeric',
															                    month: 'short',
															                    year: 'numeric'
															                }).replace(/ /g, ' ')}</b>
								</div>
		            <p class="review-text" style="margin-top:5px;">${review.reviewText || 'No review text provided'}</p>
		            
		            ${review.reviewResponse ? `<p class="review-response" style="margin-top:5px;">Replied to you : ${review.reviewResponse}</p>` : ''}
		        	<br>
					<hr>
					</div>
		    `).join('')
		    : `<p>No reviews available for this restaurant.</p>`;



    } catch (error) {
        console.error('Error fetching reviews:', error);
        document.querySelector('.reviews-container').innerHTML = `<p>Error loading reviews. Please try again later.</p>`;
    }
};

const btnAddress = document.getElementById("address");
const btnReview = document.getElementById("reviews");


const addressContainer = document.querySelector('.address-container');

const reviewContainer = document.querySelector('.reviews-container');

btnAddress.addEventListener("click", () => {
    addressContainer.classList.add('visible');
	console.log(addressContainer);
	addressContainer.style.setProperty('display', 'flex', 'important');

	
    addressContainer.classList.remove('hidden');
    reviewContainer.classList.add('hidden');
    reviewContainer.classList.remove('visible');
    
    btnAddress.style.borderBottom = "2px solid #27ae60";
    btnReview.style.borderBottom = "none";
});

btnReview.addEventListener("click", () => {
    reviewContainer.classList.add('visible');
    reviewContainer.classList.remove('hidden');
    addressContainer.classList.add('hidden');
    addressContainer.classList.remove('visible');
	addressContainer.style.setProperty('display', 'none', 'important');
    
    btnReview.style.borderBottom = "2px solid #27ae60";
    btnAddress.style.borderBottom = "none";
});


document.addEventListener('DOMContentLoaded', function() {
    getFoodItems();
});

