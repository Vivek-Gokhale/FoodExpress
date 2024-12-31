document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("deliveryPartnerLoginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", async (event) => {
            event.preventDefault(); // Prevent the default form submission

            // Collect form data
            const email = document.getElementById("loginEmail").value;
            const password = document.getElementById("loginPassword").value;

            // Prepare payload for the POST request
            const payload = {
                email: email,
                password: password
            };

            try {
                // Send the POST request
                const response = await fetch("/login-delivery-partner", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                });

                // Handle response
                if (response.ok) {
                    const message = await response.text();
                    alert("Success: " + message);
                    // Redirect to the delivery partner dashboard or another page
                    window.location.href = "/";
                } else {
                    const errorMessage = await response.text();
                    alert("Error: " + errorMessage);
                }
            } catch (error) {
                console.error("Error occurred:", error);
                alert("An error occurred while trying to log in. Please try again.");
            }
        });
    } else {
        console.error("Login form not found.");
    }
});
