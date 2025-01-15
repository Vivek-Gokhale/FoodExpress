document.addEventListener("DOMContentLoaded", () => {
	
	
    const partnerId = localStorage.getItem("deliveryPartnerIdPartner"); // Replace with the actual partnerId

    // Fetch and populate Available Orders
    fetch("get-pending-order")
        .then(response => response.json())
        .then(data => populateAvailableOrders(data))
        .catch(error => console.error("Error fetching available orders:", error));

    // Fetch and populate Pending Orders
    fetch(`get-partner-pending-orders/${partnerId}`)
        .then(response => response.json())
        .then(data => populatePendingOrders(data))
        .catch(error => console.error("Error fetching pending orders:", error));

    // Fetch and populate Completed Orders
    fetch(`get-partner-completed-orders/${partnerId}`)
        .then(response => response.json())
        .then(data => populateCompletedOrders(data))
        .catch(error => console.error("Error fetching completed orders:", error));

    // Function to populate Available Orders table
    function populateAvailableOrders(orders) {
        const table = document.getElementById("availableOrdersTable");
        const tbody = document.createElement("tbody");
        table.querySelector("tbody")?.remove(); // Remove existing tbody if any
        orders.forEach(order => {
            const row = document.createElement("tr");
			row.id = `row${order.orderId}`;
            row.innerHTML = `
                <td>${order.orderId}</td>
                <td>${order.userId}</td>
                <td>${order.address}</td>
                <td><button class="accept-order" data-order-id="${order.orderId}">Accept</button></td>
            `;
            tbody.appendChild(row);
        });
        table.appendChild(tbody);
    }

    // Function to populate Pending Orders table
	function populatePendingOrders(orders) {
	    const table = document.getElementById("pendingOrdersTable");
	    const tbody = document.createElement("tbody");
	    table.querySelector("tbody")?.remove(); // Remove existing tbody if any

	    orders.forEach(order => {
	        const row = document.createElement("tr");
	        row.innerHTML = `
	            <td>${order.orderId}</td>
	            <td>${order.userId}</td>
	            <td>${order.userName}</td>
	            <td>${order.phoneNo}</td>
	            <td>${order.address}</td>
	            <td>${order.totalAmount}</td>
	            <td>${order.paymentOption}</td>
	            <td>Pending</td>
	            <td>
	                <button 
	                    class="mark-complete" 
	                    data-order-id="${order.orderId}" 
	                    data-user-id="${order.userId}" 
	                    data-delivery-id="${order.deliveryId || ''}">
	                    Send OTP
	                </button>
	            </td>
	        `;
	        tbody.appendChild(row);
	    });

	    table.appendChild(tbody);
	}


    // Function to populate Completed Orders table
    function populateCompletedOrders(orders) {
        const table = document.getElementById("completedOrdersTable");
        const tbody = document.createElement("tbody");
        table.querySelector("tbody")?.remove(); // Remove existing tbody if any
        orders.forEach(order => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${order.orderId}</td>
                <td>${order.userId}</td>
                <td>${order.userName}</td>
                <td>${order.phoneNo}</td>
                <td>${order.address}</td>
                <td>${order.totalAmount}</td>
                <td>${order.paymentOption}</td>
                <td>Completed</td>
            `;
            tbody.appendChild(row);
        });
        table.appendChild(tbody);
    }

    // Event delegation for Accept and Mark as Completed buttons
    document.body.addEventListener("click", event => {
		if (event.target.classList.contains("accept-order")) {
		        const orderId = event.target.dataset.orderId;
		        const partnerId = localStorage.getItem("deliveryPartnerIdPartner");

		        // Use the Geolocation API to get the current position
		        if (navigator.geolocation) {
		            navigator.geolocation.getCurrentPosition(
		                (position) => {
		                    const longitude = position.coords.longitude.toString();
		                    const latitude = position.coords.latitude.toString();

		                    // Call API to accept the order
		                    fetch("select-delivery-order", {
		                        method: "POST",
		                        headers: {
		                            "Content-Type": "application/json"
		                        },
		                        body: JSON.stringify({
		                            partnerId: parseInt(partnerId),
		                            orderId: parseInt(orderId),
		                            longitude: longitude,
		                            latitude: latitude
		                        })
		                    })
		                        .then(response => {
		                            if (!response.ok) {
		                                throw new Error("Failed to accept order.");
		                            }
		                            return response.json();
		                        })
		                        .then((isAccepted) => {
		                            if (isAccepted) {
		                                console.log(`Order with ID: ${orderId} accepted.`);
		                                // Remove the row for the accepted order
										const orderRow = document.getElementById(`row${orderId}`);
										if (orderRow) {
										    orderRow.remove();
										}


		                                // Refresh the tables
		                                fetch("get-pending-order")
		                                    .then(response => response.json())
		                                    .then(data => populateAvailableOrders(data));

		                                fetch(`get-partner-pending-orders/${partnerId}`)
		                                    .then(response => response.json())
		                                    .then(data => populatePendingOrders(data));
		                            } else {
		                                console.error("Order acceptance failed.");
		                            }
		                        })
		                        .catch(error => console.error("Error accepting order:", error));
		                },
		                (error) => {
		                    console.error("Error fetching location:", error.message);
		                }
		            );
		        } else {
		            console.error("Geolocation is not supported by this browser.");
		        }
		    }

			if (event.target.classList.contains("mark-complete")) {
			    const orderId = event.target.dataset.orderId;
			    const userId = event.target.dataset.userId;
			    const deliveryId = event.target.dataset.deliveryId;

			    // Call API to send OTP
			    fetch(`send-delivery-otp/${userId}`, { method: "POST" })
			        .then(response => {
			            if (!response.ok) {
			                throw new Error("Failed to send OTP.");
			            }
			            return response.text();
			        })
			        .then(() => {
			            console.log(`OTP sent to user with ID: ${userId}.`);

			            // Create the modal dynamically
			            const modal = document.createElement("div");
			            modal.id = "otpModal";
			            modal.style.cssText = `
			                position: fixed;
			                top: 50%;
			                left: 50%;
			                transform: translate(-50%, -50%);
			                background-color: white;
			                border: 1px solid #ccc;
			                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
			                padding: 20px;
			                z-index: 1000;
			                width: 300px;
			                text-align: center;
			                border-radius: 10px;
			            `;

			            modal.innerHTML = `
			              
			                <input type="text" id="otpInput" placeholder="Enter OTP" style="width: 90%; margin-bottom: 10px; padding: 8px; border: 1px solid #ccc; border-radius: 5px;">
			                <button class="verify-otp" style="margin: 5px; padding: 8px 16px; border: none; border-radius: 5px; background-color: #1E712A; color: white;">Verify</button>
			                <button class="close-modal" style="margin: 5px; padding: 8px 16px; border: none; border-radius: 5px; background-color: #233040; color: white;">Close</button>
			            `;

			            // Create the overlay background
			            const overlay = document.createElement("div");
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

			            // Append modal to body
			            document.body.appendChild(modal);

			            // Close modal function
			            const closeModal = () => {
			                modal.remove();
			                overlay.remove();
			            };

			            // Close modal event listener
			            modal.querySelector(".close-modal").addEventListener("click", closeModal);

			            // OTP verification button event listener
			            modal.querySelector(".verify-otp").addEventListener("click", () => {
			                const otp = modal.querySelector("#otpInput").value.trim();

			                if (!otp) {
			                    alert("Please enter the OTP.");
			                    return;
			                }

			                // Call the API to verify OTP
			                fetch(`verify-otp/${deliveryId}/${userId}/${otp}/${orderId}`, { method: "PUT" })
			                    .then(response => {
			                        if (!response.ok) {
			                            throw new Error("Invalid OTP or verification failed.");
			                        }
			                        return response.text();
			                    })
			                    .then((message) => {
			                        console.log(message);

			                        // Close modal and overlay
			                        closeModal();

			                        // Refresh the tables
			                        fetch(`get-partner-pending-orders/${localStorage.getItem("deliveryPartnerIdPartner")}`)
			                            .then(response => response.json())
			                            .then(data => populatePendingOrders(data));
			                        fetch(`get-partner-completed-orders/${localStorage.getItem("deliveryPartnerIdPartner")}`)
			                            .then(response => response.json())
			                            .then(data => populateCompletedOrders(data));
			                    })
			                    .catch(error => console.error("Error verifying OTP:", error));
			            });
			        })
			        .catch(error => console.error("Error sending OTP:", error));
			}

    });

	const isLoggedIn = localStorage.getItem("isLoggedInPartner");

	    if (isLoggedIn === "true") {
	        const partnerId = localStorage.getItem("deliveryPartnerIdPartner"); // Assuming partnerId is stored in localStorage

	        // Function to send the update-location request
	        const updateLocation = (longitude, latitude) => {
	            const locationData = {
	                partnerId: parseInt(partnerId),
					longitude: longitude.toString(),
					 latitude: latitude.toString()
	            };

	            fetch("update-location", {
	                method: "PUT",
	                headers: {
	                    "Content-Type": "application/json"
	                },
	                body: JSON.stringify(locationData)
	            })
	                .then(response => {
	                    if (!response.ok) {
	                        throw new Error("Failed to update location");
	                    }
	                    return response.json();
	                })
	                .then(result => {
	                    console.log("Location updated:", result);
	                })
	                .catch(error => {
	                    console.error("Error updating location:", error);
	                });
	        };

	        // Function to get the current location using the Geolocation API
	        const getCurrentLocation = () => {
	            if (navigator.geolocation) {
	                navigator.geolocation.getCurrentPosition(
	                    position => {
	                        const longitude = position.coords.longitude;
	                        const latitude = position.coords.latitude;
	                        updateLocation(longitude, latitude);
	                    },
	                    error => {
	                        console.error("Error getting geolocation:", error);
	                    }
	                );
	            } else {
	                console.error("Geolocation is not supported by this browser.");
	            }
	        };

	        // Call the function every 10 seconds
	        const intervalId = setInterval(getCurrentLocation, 10000);
			document.getElementById("logoutBtn").addEventListener("click", () => {
			            localStorage.removeItem("isLoggedInPartner");
						localStorage.removeItem("deliveryPartnerIdPartner");
						localStorage.removeItem("emailPartner");
						
						window.location.href = "/";
			            clearInterval(intervalId);
			        });
		}
		
		
});




