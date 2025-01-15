const addAddressBtn = document.getElementById("add-address-btn");

addAddressBtn.addEventListener('click', (event) => {
    // Prevent the form from submitting the usual way
    event.preventDefault();

    // Retrieve userId from localStorage
    const userId = localStorage.getItem('userIdCustomer');
    if (!userId) {
        alert('User ID is missing.');
        return;
    }

    // Get form data
    const pincode = document.getElementById("customerAddressPincode").value;
    const houseApartment = document.getElementById("customerAddressHouseApartment").value;
    const area = document.getElementById("customerAddressArea").value;
    const landmark = document.getElementById("customerAddressLandmark").value;
    const townCity = document.getElementById("customerAddressTownCity").value;
    const state = document.getElementById("customerAddressState").value;
    const isDefault = false; // You can change this based on your use case

    // Create the CustomerAddress object
    const customerAddress = {
        userId: userId,
        pincode: pincode,
        houseApartment: houseApartment,
        area: area,
        landmark: landmark,
        townCity: townCity,
        state: state,
        isDefault: isDefault
    };

    // Send data to the backend via fetch
    fetch("/add-customer-address", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(customerAddress),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Address added successfully");
			document.getElementById("cus-add-form").reset();
			window.location.href="/user-address";
        } else {
            alert("Failed to add address");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Error processing request.");
    });
});
