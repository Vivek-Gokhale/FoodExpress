document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".form");

    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent the default form submission behavior

        // Collect form data
        const restaurantData = {
            name: form.querySelector('input[name="name"]').value.trim(),
            email: form.querySelector('input[name="email"]').value.trim(),
            phoneNo: form.querySelector('input[name="phoneNo"]').value.trim(),
            registrationDate: form.querySelector('input[name="registrationDate"]').value,
            startTime: form.querySelector('input[name="startTime"]').value,
            closeTime: form.querySelector('input[name="closeTime"]').value,
        };

        try {
            // Send POST request
            const response = await fetch("/restaurant-register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(restaurantData),
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const result = await response.json();

            // Handle response
            if (result > 0) {
				localStorage.setItem("restaurantId", result);
                alert(`Restaurant registered successfully with ID: ${result}`);
                window.location.href = `add-admin?restaurantId=${result}`;
            } else {
                alert("Failed to register the restaurant. Please try again.");
            }
        } catch (error) {
            console.error("Error registering restaurant:", error);
            alert("An error occurred while registering the restaurant. Please try again later.");
        }
    });
});
