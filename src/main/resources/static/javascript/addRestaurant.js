let lon = "";
let lat = "";
let address = "";

document.addEventListener("DOMContentLoaded", () => {
    let map, marker, autocomplete;

    function initMap() {
        // Default location: India's approximate center
        const defaultLocation = { lat: 20.5937, lng: 78.9629 };
        map = new google.maps.Map(document.getElementById("map"), {
            center: defaultLocation,
            zoom: 6,
        });

        // Initialize autocomplete
        const input = document.getElementById("searchInput");
        autocomplete = new google.maps.places.Autocomplete(input);
        autocomplete.setFields(["geometry", "formatted_address"]);

        // Add listener for when the user selects a suggestion
        autocomplete.addListener("place_changed", function () {
            const place = autocomplete.getPlace();

            if (!place.geometry) {
                alert("No details available for address " + place.name);
                return;
            }

            // Update map and marker
            const location = place.geometry.location;
            map.setCenter(location);
            map.setZoom(14);

            if (marker) {
                marker.setMap(null);
            }

            marker = new google.maps.Marker({
                map: map,
                position: location,
            });

            // Store longitude and latitude
            lon = location.lng();
            lat = location.lat();

            // Store the formatted address
            address = place.formatted_address;
			

            console.log(`Longitude: ${lon}, Latitude: ${lat}, Address: ${address}`);
        });
    }

    // Initialize the map when the page loads
    initMap();

    // Handle form submission
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
            address: address,
            location: `${lat},${lon}`, // Store as latitude,longitude
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
