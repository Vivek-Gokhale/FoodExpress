document.addEventListener('DOMContentLoaded', function() {
  
    const urlParams = new URLSearchParams(window.location.search);
    const aid = urlParams.get('aid'); // Assuming the URL contains ?aid=<address_id>
	
    // Fetch the customer address data using the aid
    if (aid) {
        fetch(`/get-customer-address/${aid}`)
            .then(response => response.json())
            .then(data => {
                if (data) {
                    // Set the form fields with the fetched data
				
                    document.getElementById("customerAddressPincode").value = data.pincode;
                    document.getElementById("customerAddressHouseApartment").value = data.houseApartment;
                    document.getElementById("customerAddressArea").value = data.area;
                    document.getElementById("customerAddressLandmark").value = data.landmark || '';
                    document.getElementById("customerAddressTownCity").value = data.townCity;
                    document.getElementById("customerAddressState").value = data.state;
					document.getElementById("aid").value = data.default;
					console.log(document.getElementById("aid").value);
                }
            })
            .catch(error => {
                console.error("Error fetching address:", error);
                alert("Failed to load address data.");
            });
    }

    const saveAddressBtn = document.getElementById('save-address-btn');
    saveAddressBtn.addEventListener('click', function(event) {

        const updatedAddress = {
            aid: aid,
            userId: localStorage.getItem("userIdCustomer"), // Assuming the userId is stored in localStorage
            pincode: document.getElementById("customerAddressPincode").value,
            houseApartment: document.getElementById("customerAddressHouseApartment").value,
            area: document.getElementById("customerAddressArea").value,
            landmark: document.getElementById("customerAddressLandmark").value,
            townCity: document.getElementById("customerAddressTownCity").value,
            state: document.getElementById("customerAddressState").value,
            
        };

		const isDefault = document.getElementById("aid").value;
        fetch(`/edit-customer-address/${isDefault}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(updatedAddress),
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Address Edited Successfully"); // Show success message
				window.location.href = "/user-address";
            } else {
                alert(data.message); // Show failure message
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
    });
});
