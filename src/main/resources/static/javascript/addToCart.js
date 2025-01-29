document.addEventListener('DOMContentLoaded', function() {
	
	let cartItemCount = 0;
    const userId = localStorage.getItem('userIdCustomer'); // Get userId from localStorage
    if (!userId) {
        console.error("User is not logged in");
        return;
    }
	const BasketEle = document.getElementById("BasketEle");
	const orderSum = document.getElementById("orderSum");
	const emptyCon = document.getElementById("emptyCon");
	
	
    // Fetch the user's cart items from the API
    fetch(`/get-my-cart/${userId}`)
        .then(response => {
            if (!response.ok) {
				emptyCon.style.display = "block";
                throw new Error('Failed to fetch cart items');
				
            }
			
            return response.json();
        })
        .then(userCartList => {
			BasketEle.style.display = 'block';  // Makes the element visible
			orderSum.style.display = 'block';   // Makes the element visible
			
            const foodItemList = document.getElementById('foodItemList');
            const basketItemTemplate = document.getElementById('BasketItemTemplate');

            userCartList.forEach(item => {
				cartItemCount = cartItemCount+1;
				addItemForSpecialRequest(item);
                const templateClone = basketItemTemplate.content.cloneNode(true);

                const foodImageLink = templateClone.querySelector('#singleFoodItem');
                foodImageLink.href = `/single-food-page?itemId=${item.itemId}`;
                foodImageLink.dataset.id = item.itemId;

                const foodImage = templateClone.querySelector('.basket-food-image');
                foodImage.src = `/images_RestaurantM/${item.image}`;
                foodImage.alt = item.itemName;

                templateClone.querySelector('.basketFoodItemName').textContent = item.itemName;
                const priceElement = templateClone.querySelector('.basketFoodItemPrice');
                priceElement.textContent = `₹${item.price * item.quantity}`;
                templateClone.querySelector('.category').textContent = "In Stock";
                const quantityElement = templateClone.querySelector('.productQuantity');
                quantityElement.textContent = item.quantity;

                const decrementButton = templateClone.querySelector('.cartDecrement');
                const incrementButton = templateClone.querySelector('.cartIncrement');

                decrementButton.addEventListener('click', function() {
                    updateCartQuantity(item, 0, quantityElement, priceElement);
                });

                incrementButton.addEventListener('click', function() {
                    updateCartQuantity(item, 1, quantityElement, priceElement);
                });

                const removeButton = templateClone.querySelector('#removeCartItem');
                removeButton.addEventListener('click', function() {
                    removeItemFromCart(item.cartItemId, item.itemId);
					cartItemCount = cartItemCount-1;
					if(cartItemCount == 0)
						{
							BasketEle.style.display = 'none';  // Makes the element visible
							orderSum.style.display = 'none';
							emptyCon.style.display = "block";  
						}
                });

                foodItemList.appendChild(templateClone);
				
            });
			loadOrderSummary(localStorage.getItem("userIdCustomer"));
			const proceedBtn = document.getElementById("proceedToPay");
			const paymentPopup = document.getElementById("paymentPopup");
			const applyCoupon = document.getElementById("applyCoupon");

			applyCoupon.addEventListener('click', () => {
			    const couponCode = document.getElementById("couponCode").value;
			    const userId = localStorage.getItem('userIdCustomer'); // Or use a different way to get userId

			    // Verify coupon first using GET (for the verification step)
			    fetch(`/verify-coupon/${couponCode}`)
			        .then(response => {
			            if (!response.ok) {
			                throw new Error('Coupon verification failed');
			            }
			            return response.text(); // Use text() instead of json() because the response is a string
			        })
			        .then(verificationMessage => {
			            if (verificationMessage === "Coupon is valid") {
			                // Coupon is valid, now apply the coupon using POST (couponCode in URL)
			                fetch(`/apply-coupon/${userId}/${couponCode}`, {
			                    method: 'POST', // Use POST method
			                })
			                    .then(response => response.text())
			                    .then(resultMessage => {
			                        alert(resultMessage); // Display the result from the applyCoupon handler
			                    })
			                    .catch(error => {
			                        console.error('Error applying coupon:', error);
			                        alert('Failed to apply coupon');
			                    });
			            } else {
			                alert('Invalid coupon');
			            }
			        })
			        .catch(error => {
			            console.error('Error verifying coupon:', error);
			            alert('Invalid coupon');
			        });
			});
			
			
			const placeOrder = document.getElementById("placeOrder");
			placeOrder.addEventListener('click', () => {
			    const items = collectItemsForOrder();
			    const userId = localStorage.getItem('userIdCustomer');
			    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
			    const paymentFlag = paymentMethod === 'online' ? 1 : 0;

			    if (paymentFlag === 1) {
			        fetch(`/place-order/${userId}/1`, {
			            method: 'POST',
			            headers: { 'Content-Type': 'application/json' },
			            body: JSON.stringify(items)
			        })
			        .then(response => response.json())
			        .then(orderResponse => {
			            resetOrderSummary();
						
			            if (orderResponse.id) {
			                const razorpayOptions = {
			                    key: 'rzp_test_s2VG2G2HwcOQd6',
			                    amount: orderResponse.amount,
			                    currency: 'INR',
			                    order_id: orderResponse.id,
			                    name: 'Food Express',
			                    description: 'Payment for Order',
			                    handler: function (response) {
			                        const paymentDetails = {
			                            razorpay_payment_id: response.razorpay_payment_id,
			                            razorpay_order_id: response.razorpay_order_id,
			                            razorpay_signature: response.razorpay_signature
			                        };

			                        fetch('/verify-payment', {
			                            method: 'POST',
			                            headers: { 'Content-Type': 'application/json' },
			                            body: JSON.stringify(paymentDetails)
			                        })
			                        .then(res => res.json())
			                        .then(verificationResponse => {
			                            if (verificationResponse === 'Payment verified successfully!') {
			                                alert('Payment successful and order confirmed!');
			                                resetOrderSummary();
			                                window.location.href = "/add-to-cart";
			                            } else {
			                                alert('Payment verification failed');
			                            }
			                        })
			                        .catch(error => console.error('Payment verification error:', error));
			                    },
			                    prefill: {
			                        name: localStorage.getItem('usernameCustomer'),
			                        email: localStorage.getItem('emailCustomer'),
			                        contact: '9898442387'
			                    },
			                    theme: {
			                        color: '#5bb450'
			                    }
			                };
			                const rzp = new Razorpay(razorpayOptions);
			                rzp.open();
							
			            } else {
			                alert('Failed to create Razorpay order');
			            }
			        })
			        .catch(error => {
			            console.error('Error placing online order:', error);
			            alert('Failed to place the order');
			        });
			    } else {
			        fetch(`/place-order/${userId}/0`, {
			            method: 'POST',
			            headers: { 'Content-Type': 'application/json' },
			            body: JSON.stringify(items)
			        })
			        .then(response => response.text()) // Use .text() if the response is plain text
			        .then(orderResponse => {
			            resetOrderSummary();
			            if (orderResponse === 'cod') {
			                alert('Order placed successfully via Cash on Delivery');
			                window.location.href = "/add-to-cart";
			            } else {
			                alert('Failed to place the order');
			            }
			        })
			        .catch(error => {
			            console.error('Error placing COD order:', error);
			            alert('Failed to place the order');
			        });
			    }
			});



			
			proceedBtn.addEventListener('click', () => {
			    paymentPopup.style.display = 'flex'; // Set to flex to make it visible
			    paymentPopup.style.opacity = '0'; // Start with opacity 0
			    setTimeout(() => {
			        paymentPopup.style.opacity = '1'; // Smooth fade-in after short delay
			    }, 100); // Delay in milliseconds
			});

			
			// Optional: Close popup when clicking outside the content
			paymentPopup.addEventListener('click', (event) => {
			    if (event.target === paymentPopup) {
			        paymentPopup.style.opacity = '0'; // Smooth fade-out
			        setTimeout(() => {
			            paymentPopup.style.display = 'none'; // Hide after fade-out
			        }, 300);
			    }
			});


			
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

function updateCartQuantity(item, flag, quantityElement, priceElement) {
    fetch(`/update-cart/${flag}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            cartItemId: item.cartItemId,
            userId: item.userId,
            itemId: item.itemId,
            quantity: item.quantity
        })
    })
    .then(response => {
        if (response.ok) {
            response.json().then(updatedCart => {
                const updatedItem = updatedCart.find(cart => cart.cartItemId === item.cartItemId);
                if (updatedItem) {
                    quantityElement.textContent = updatedItem.quantity;
                    const newPrice = item.price * updatedItem.quantity;
                    priceElement.textContent = `₹${newPrice}`;
                    item.quantity = updatedItem.quantity; // Update the item's quantity reference
					loadOrderSummary(localStorage.getItem("userIdCustomer"));
                }
            });
        } else {
            alert("Failed to update cart quantity");
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function removeItemFromCart(cartItemId, itemId) {
    const userId = localStorage.getItem('userIdCustomer');
    if (!userId) {
        console.error("User is not logged in");
        return;
    }

    fetch(`/remove-from-cart/${cartItemId}/${userId}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('basketFoodCartValue').remove();
			loadOrderSummary(localStorage.getItem("userIdCustomer"));
			removeItemFromSpecialRequest(itemId);
            
        } else {
            alert("Failed to remove item from cart");
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

async function loadOrderSummary(userId) {
    try {
        const response = await fetch(`/get-order_summary/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Error fetching order summary: ${response.statusText}`);
        }

        const orderSummary = await response.json();

        // Populate the order summary details in the UI
        document.getElementById('totalAmount').textContent = `₹${orderSummary.totalAmount.toFixed(2)}`;
        document.getElementById('couponDiscount').textContent = `₹${orderSummary.couponDiscount.toFixed(2)}`;
        document.getElementById('tax').textContent = `₹${orderSummary.tax.toFixed(2)}`;
        document.getElementById('savings').textContent = `₹${orderSummary.savings.toFixed(2)}`;
        document.getElementById('finalAmount').textContent = `₹${orderSummary.finalAmount.toFixed(2)}`;
    } catch (error) {
        console.error('Failed to load order summary:', error);
        alert('Unable to load order summary. Please try again later.');
    }
}
function addItemForSpecialRequest(item) {
    const table = document.querySelector(".special-request table");

    if (!table.querySelector("thead")) {
        const thead = document.createElement("thead");
        const headerRow = document.createElement("tr");

        const dishHeader = document.createElement("th");
        dishHeader.textContent = "Dish Name";
        dishHeader.style.backgroundColor = "#f4f4f4"; // Background color for the header
        dishHeader.style.padding = "8px";
		dishHeader.style.fontSize="16px";

        const requestHeader = document.createElement("th");
        requestHeader.textContent = "Special Request";
        requestHeader.style.backgroundColor = "#f4f4f4"; // Background color for the header
        requestHeader.style.padding = "8px";
		requestHeader.style.fontSize="16px";

        headerRow.appendChild(dishHeader);
        headerRow.appendChild(requestHeader);

        thead.appendChild(headerRow);
        table.appendChild(thead);
    }

    const row = document.createElement("tr");

    const dishCell = document.createElement("td");
    dishCell.style.padding = "8px";
    dishCell.textContent = item.itemName; 
	dishCell.style.fontSize="16px";// Assuming item has a 'itemName' property

    const inputCell = document.createElement("td");


    const input = document.createElement("input");
   
    input.type = "text";
	input.style.padding = "10px";
	input.style.borderRadius="4px";
    input.placeholder = "Special request or additional instructions";
    input.style.width = "100%";
	input.style.fontSize="16px";
	input.style.border="1px solid #ddd";

    inputCell.appendChild(input);

    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.value = item.itemId; // Store itemId in the hidden input
	row.id = `item${item.itemId}`;
    inputCell.appendChild(hiddenInput);

    row.appendChild(dishCell);
    row.appendChild(inputCell);

    table.appendChild(row);
}


function removeItemFromSpecialRequest(itemId) {
    const rowId = `item${itemId}`; 
    const row = document.getElementById(rowId); 
    
    if (row) {
        row.parentNode.removeChild(row); 
    } else {
        console.log(`Row with ID ${rowId} not found.`);
    }
}


function collectItemsForOrder() {
    const items = [];
    const rows = document.querySelectorAll(".special-request table tr");
    
    // Skip the header row
    for (let i = 1; i < rows.length; i++) {
        const row = rows[i];
        const itemId = row.querySelector("input[type='hidden']").value;
        const specialRequest = row.querySelector("input[type='text']").value;

        // Create item object and push it to the items array
        items.push({
            itemId: parseInt(itemId), // Convert itemId to integer
            specialRequest: specialRequest
        });
    }

    return items;
}


function resetOrderSummary() {
    // Hide payment popup
	
    const paymentPopup = document.getElementById('paymentPopup');
    if (paymentPopup) {
        paymentPopup.style.display = 'none';
    }

    // Clear food item list table body
    const foodItemList = document.getElementById('foodItemList');
    if (foodItemList) {
        foodItemList.innerHTML = '';  // Remove all rows from table body
    }

    // Reset order summary values to 0
    document.getElementById('totalAmount').textContent = '₹0.00';
    document.getElementById('couponDiscount').textContent = '₹0.00';
    document.getElementById('tax').textContent = '₹0.00';
    document.getElementById('savings').textContent = '₹0.00';
    document.getElementById('finalAmount').textContent = '₹0.00';
	
	document.getElementById("BasketEle").style.display = "none";
	document.getElementById("emptyCon").style.display = "block";
	document.getElementById("orderSum").style.display = "none";
}
