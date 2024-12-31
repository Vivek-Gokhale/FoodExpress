document.addEventListener("DOMContentLoaded", () => {
    const addAdminForm = document.getElementById("addAdminForm");

    addAdminForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Get restaurantId from the query parameter
        const urlParams = new URLSearchParams(window.location.search);
        const restaurantId = urlParams.get("restaurantId");

        if (!restaurantId) {
            alert("Restaurant ID is missing. Please register the restaurant first.");
            return;
        }

        // Get form data
        const email = document.querySelector('input[name="email"]').value;
        const username = document.querySelector('input[name="username"]').value;
        const password = document.querySelector('input[name="password"]').value;

        // Prepare the admin data
        

        try {
            // Make the POST request
            const response = await fetch("/add-admin", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ restaurantId, email, username, password }),
            });

            const message = await response.text();

            if (response.ok) {
                alert(message);
                window.location.href = '/';
            } else {
                alert(`Error: ${message}`);
            }
        } catch (error) {
            console.error("Error adding admin:", error);
            alert("An error occurred while adding the admin. Please try again.");
        }
    });
});
