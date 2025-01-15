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

                if (response.ok) {
                    const data = await response.json();

                    // Store details in localStorage
                    localStorage.setItem("deliveryPartnerIdPartner", data.deliveryPartnerId);
                    localStorage.setItem("emailPartner", data.email);
                    localStorage.setItem("isLoggedInPartner", true);

                    // Redirect with name as a query parameter
                    const queryParam = new URLSearchParams({ name: data.name }).toString();
                    window.location.href = `/delivery-partner-dashboard?${queryParam}`;
                } else {
                    const errorData = await response.json();
                    alert("Error: " + errorData.error || "Authentication failed.");
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
