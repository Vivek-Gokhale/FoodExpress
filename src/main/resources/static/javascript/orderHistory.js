let overlay;
document.addEventListener("DOMContentLoaded", async () => {
  const userId = localStorage.getItem("userIdCustomer"); // Replace with dynamic userId if needed
  const orderContainer = document.querySelector(".order-container");
  const orderTemplate = document.getElementById("order-template");
  const orderItemTemplate = document.getElementById("order-item-template");

  try {
    const response = await fetch(`/get-orders/${userId}`);
    if (!response.ok) {
      throw new Error(`Error fetching orders: ${response.statusText}`);
    }
    const orders = await response.json();
    console.log(orders);

    for (const order of orders) {
      const orderClone = orderTemplate.content.cloneNode(true);

      // Format and set order date
      const orderDate = new Date(order.orderDate);
      const options = { day: '2-digit', month: 'short', year: 'numeric' }; // e.g., 12 Dec 2024
      const formattedDate = orderDate.toLocaleDateString('en-GB', options);
      orderClone.querySelector(".order-date").textContent = formattedDate;

      // Set other order details
      orderClone.querySelector(".order-status").textContent = order.deliveryStatus === "completed" ? "Completed" : "Pending";
      if (order.deliveryStatus === "completed") {
        orderClone.querySelector(".order-status").style.backgroundColor = "#D4F6D4";
      }
      orderClone.querySelector(".order-total").textContent = order.amount.toFixed(2);
      orderClone.querySelector(".order-id").textContent = order.orderId;

      const orderProductsContainer = orderClone.querySelector(".order-products");
      let flag = false;
      let trackResponse;

      for (const item of order.orderItems) {
        const itemClone = orderItemTemplate.content.cloneNode(true);
        itemClone.getElementById("singleFoodItem").href = `single-food-page?itemId=${item.itemId}`;
        const imageUrl = `/images_RestaurantM/${item.image}`; // Adjust path if needed
        itemClone.querySelector(".product-image").src = imageUrl;
        itemClone.querySelector(".product-name").textContent = item.itemName;
        itemClone.querySelector(".product-quantity").textContent = item.quantity;

        const reviewHolder = itemClone.querySelector("#reviewHolder");

        if (order.deliveryStatus === "pickedup" && !flag) {
          // Call check-live-tracking-con API once per order
          try {
            trackResponse = await fetch(`/check-live-tracking-con/${order.orderId}`);
            if (trackResponse.ok) {
              flag = true;
            }
          } catch (trackError) {
            console.error("Failed to check live tracking condition:", trackError);
          }
        }

        if (order.deliveryStatus === "completed") {
          const alreadyReviewed = await isAlreadyReviewed(userId, order.orderId, item.restaurantId); // Await for the async call
          if (!alreadyReviewed) {
            const button = document.createElement("button");
            button.style.marginTop = "10px";
            button.className = "review";
            button.textContent = "Write review";
            button.addEventListener("click", () => {
              window.location.href = `/create-review?uid=${userId}&rid=${item.restaurantId}&itemname=${item.itemName}&image=${item.image}&rname=${item.restaurantName}&mid=${item.itemId}&oid=${item.orderId}`;
            });
            reviewHolder.appendChild(button);
          } else {
            const message = document.createElement("p");
            message.style.marginTop = "10px";
            message.className = "already-reviewed";
            message.innerHTML = "<i class='fa-solid fa-circle-check fa-lg'></i> Already reviewed";
            reviewHolder.appendChild(message);
          }
        }
        orderProductsContainer.appendChild(itemClone);
      }

      // Add Track Order button once if flag is true
      if (flag) {
        const trackButton = document.createElement("button");
        trackButton.style.marginTop = "10px";
		trackButton.dataset.orderId = order.orderId;
        trackButton.className = "track-order";
        trackButton.textContent = "Track your order";
		trackButton.classList.add("review");
		orderProductsContainer.parentNode.insertBefore(trackButton, orderProductsContainer.nextSibling);

		trackButton.addEventListener("click", async () => {
		    const userId = localStorage.getItem("userIdCustomer");  
		    console.log("User ID:", userId);
		    
		    const orderId = trackButton.dataset.orderId;  
		    console.log("Order ID:", orderId);

		    if (!userId) {
		        alert("User ID is not available. Please log in.");
		        return;
		    }

		    try {
		        const response = await fetch(`/get-customer-addresses/${userId}`);
		        if (!response.ok) {
		            throw new Error("Failed to load addresses.");
		        }
		        const addresses = await response.json();
		        const defaultAddress = addresses.find(address => address.default);
		        
		        if (defaultAddress) {
		            console.log("Default Address:", defaultAddress);
		            const coordinates = await getCoordinates(defaultAddress);
					if (coordinates.latitude && coordinates.longitude) {
					                const deliveryResponse = await fetch(`/get-delivery-partner/${orderId}`);
					                if (!deliveryResponse.ok) {
					                    throw new Error("Failed to load delivery partner.");
					                }
					                const partner = await deliveryResponse.json();
									console.log(partner);
									const customerLocation = { lat: coordinates.latitude, lng: coordinates.longitude };
					                const partnerLocation = { lat: parseFloat(partner[0][9]), lng: parseFloat(partner[0][8])};
					                   
									console.log(customerLocation, partnerLocation);
					                document.getElementById('live-tracking-popup').style.display = 'block';
									document.getElementById('addressMapTrack').style.display = 'block';
									overlay = document.createElement("div");
												            overlay.style.cssText = `
												                position: fixed;
												                top: 0;
												                left: 0;
												                width: 100%;
												                height: 100%;
												                background-color: rgba(0, 0, 0, 0.5);
												                z-index: 999;
												            `;
												            document.body.appendChild(overlay);
					                initMap(customerLocation, partnerLocation);
					
 									
					                // Further processing or display of delivery partner information
					            } else {
					                console.error("Failed to fetch coordinates for the default address.");
					            }
		        } else {
		            console.log("No default address found.");
		        }
		    } catch (error) {
		        console.error("Error fetching customer addresses:", error);
		        alert("Failed to load customer addresses. Please try again later.");
		    }
		});


      }

      orderContainer.appendChild(orderClone);
    }
  } catch (error) {
    console.error("Failed to fetch orders:", error);
  }
});



const isAlreadyReviewed = async (userId, orderId, restaurantId) => {
  try {
    const response = await fetch(`/check-review/${userId}/${restaurantId}`);
    
    if (!response.ok) {
      // If status is 404, return false indicating review does not exist
      return false;
    }
   else return true;
  } catch (error) {
    console.error("Error checking review existence:", error);
    return false;
  }
};

async function getCoordinates(address) {
    const API_KEY = 'AlzaSyBcIpX8BWWijn5yhyQRnXquOzLErrIlDuv';  // Replace with your actual API key
    const fullAddress = `${address.houseApartment}, ${address.area}, ${address.landmark}, ${address.townCity}, ${address.state}, ${address.pincode}`;
    const url = `https://maps.gomaps.pro/maps/api/geocode/json?address=${encodeURIComponent(fullAddress)}&key=${API_KEY}`;

    try {
        const response = await fetch(url);
        const data = await response.json();
        if (data.status === 'OK') {
            const location = data.results[0].geometry.location;
            return { latitude: location.lat, longitude: location.lng };
        } else {
            console.error('Error fetching data for address:', fullAddress, data.status);
            return { latitude: null, longitude: null };
        }
    } catch (error) {
        console.error('Fetch error:', error);
        return { latitude: null, longitude: null };
    }
}

function initMap(customerLocation, partnerLocation) {
            const map = new google.maps.Map(document.getElementById("addressMapTrack"), {
                zoom: 12,
                center: partnerLocation,
            });

            const riderIcon = {
                url: "https://maps.google.com/mapfiles/ms/icons/motorcycling.png",
                scaledSize: new google.maps.Size(40, 40),
            };

            new google.maps.Marker({
                position: partnerLocation,
                map: map,
                title: "Delivery Partner Location",
                icon: riderIcon
            });

            new google.maps.Marker({
                position: customerLocation,
                map: map,
                title: "Customer Location"
            });

            const directionsService = new google.maps.DirectionsService();
            const directionsRenderer = new google.maps.DirectionsRenderer({ suppressMarkers: true });
            directionsRenderer.setMap(map);

            directionsService.route({
                origin: partnerLocation,
                destination: customerLocation,
                travelMode: 'DRIVING'
            }, function(response, status) {
                if (status === 'OK') {
                    directionsRenderer.setDirections(response);
                    const duration = response.routes[0].legs[0].duration.text;
                    document.getElementById('eta').innerHTML = `<strong>Estimated Time of Arrival (ETA):</strong> ${duration}`;
                } else {
                    document.getElementById('eta').innerHTML = '<strong>Estimated Time of Arrival (ETA):</strong> Unable to calculate';
                }
            });
        }

        document.getElementById('close-popup').onclick = () => {
            document.getElementById('live-tracking-popup').style.display = 'none';
			overlay.remove();
        };


