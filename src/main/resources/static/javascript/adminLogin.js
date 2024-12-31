document.addEventListener("DOMContentLoaded", () => {
    const adminLoginForm = document.getElementById("adminLoginForm");

    adminLoginForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent the default form submission behavior

        // Collect input values
        const restaurantEmail = document.getElementById("restaurantEmail").value;
        const adminEmail = document.getElementById("adminEmail").value;
        const password = document.getElementById("adminPassword").value;

        // Prepare the request payload
        const requestBody = {
            rEmail: restaurantEmail,
            aEmail: adminEmail,
            password: password,
        };

        try {
            // Send a POST request to the backend
            const response = await fetch("/authenticate-admin", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            });

            // Handle the response
            if (response.ok) {
                const message = await response.text();
                alert(message); // Display success message
                // Redirect or perform other actions as necessary
				window.location.href = "/dashboard";
            } else if (response.status === 401) {
                alert("Authentication failed. Please check your credentials.");
            } else {
                const errorMessage = await response.text();
                alert(`Error: ${errorMessage}`);
            }
        } catch (error) {
            console.error("Error occurred during login:", error);
            alert("An unexpected error occurred. Please try again later.");
        }
    });
});
