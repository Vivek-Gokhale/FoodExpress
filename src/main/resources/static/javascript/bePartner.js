document.addEventListener("DOMContentLoaded", () => {
    const deliveryPartnerForm = document.getElementById("deliveryPartnerForm");

    deliveryPartnerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Get form data
        const name = document.querySelector('input[name="name"]').value;
        const email = document.querySelector('input[name="email"]').value;
        const password = document.querySelector('input[name="password"]').value;
        const servingPincode = document.querySelector('input[name="servingPincode"]').value;
        const phoneNo = document.querySelector('input[name="phoneNo"]').value;
        const identityProof = document.querySelector('input[name="identityProof"]').files[0];

        // Get user's current location using the Geolocation API
        if (!navigator.geolocation) {
            alert("Geolocation is not supported by your browser.");
            return;
        }

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const currLocLongitude = position.coords.longitude;
                const currLocLatitude = position.coords.latitude;

                // Create a new FormData object to handle multipart form submission
                const formData = new FormData();
                formData.append("name", name);
                formData.append("email", email);
                formData.append("password", password);
                formData.append("servingPincode", servingPincode);
                formData.append("phoneNo", phoneNo);
                formData.append("currLocLongitude", currLocLongitude);
                formData.append("currLocLatitude", currLocLatitude);

                // Append the identity proof file
                if (identityProof) {
                    formData.append("identityProof", identityProof);
                } else {
                    alert("Please upload an identity proof.");
                    return;
                }

                try {
                    // Make the POST request with multipart/form-data
                    const response = await fetch("/register-delivery-partner", {
                        method: "POST",
                        body: formData,
                    });

                    const result = await response.json();

                    if (response.ok) {
                        alert("Delivery Partner registered successfully.");
                        // Optionally redirect or reset form
                        window.location.href = '/'; // Redirect to home page or wherever necessary
                    } else {
                        alert(`Error: ${result.message || 'Something went wrong. Please try again.'}`);
                    }
                } catch (error) {
                    console.error("Error registering delivery partner:", error);
                    alert("Partner with email already exists.");
                }
            },
            (error) => {
                alert("Unable to retrieve your location. Please enable location services.");
            }
        );
    });
});
