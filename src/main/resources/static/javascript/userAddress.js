document.addEventListener('DOMContentLoaded', function () {
    const userId = localStorage.getItem("userIdCustomer");
    const addressContainer = document.querySelector('.customer-address-container');
    const username = localStorage.getItem("usernameCustomer") || "User";

    if (!userId) {
        console.error("User ID is missing.");
        return;
    }

    fetch(`/get-customer-addresses/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load addresses.");
            }
            return response.json();
        })
        .then(addresses => {
            const defaultAddress = addresses.find(address => address.default);
            if (defaultAddress) {
                renderAddressCard(defaultAddress, true);
            }
            addresses.filter(address => !address.default)
                .forEach(address => renderAddressCard(address, false));
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Failed to load customer addresses.");
        });

    function renderAddressCard(address, isDefault) {
        const addressCard = document.createElement('div');
        addressCard.className = 'address-card';

        const defaultDiv = document.createElement('div');
        defaultDiv.className = 'address-default';
        defaultDiv.textContent = isDefault ? 'Default' : '';

        const addressDetails = document.createElement('div');
        addressDetails.className = 'address-details';
        addressDetails.innerHTML = `
            <strong>${username}</strong>
            <p>${address.houseApartment}, ${address.area}, ${address.landmark}</p>
            <p>${address.townCity} ${address.state}, ${address.pincode}</p>
            <p>Phone number: 9934840305</p>
        `;

        const addressActions = document.createElement('div');
        addressActions.className = 'address-actions';
        addressActions.innerHTML = `
            <a href="/edit-customer-address?aid=${address.aid}" class="edit">Edit</a>
            ${!isDefault ? `<a href="#" class="remove" data-aid="${address.aid}">Remove</a>` : ''}
            ${!isDefault ? `<a href="#" class="set-default" data-aid="${address.aid}" data-userid="${address.userId}">Set as default</a>` : ''}
        `;

        const removeBtn = addressActions.querySelector('.remove');  // Corrected selector for 'remove'
        if (removeBtn) {
            removeBtn.addEventListener('click', function (e) {
                e.preventDefault();
                const aid = this.getAttribute('data-aid');
                fetch(`/delete-customer-address/${aid}`, { method: 'POST' })
                    .then(response => response.text())
                    .then(result => {
                        if (result === 'success') {
                            alert('Address removed successfully.');
                            addressCard.remove();
                        } else {
                            alert('Failed to remove address.');
                        }
                    })
                    .catch(error => console.error('Error removing address:', error));
            });
        }

        const setDefaultButton = addressActions.querySelector('.set-default');
        if (setDefaultButton) {
            setDefaultButton.addEventListener('click', function (e) {
                e.preventDefault();
                const aid = this.getAttribute('data-aid');
                fetch(`/set-default-address/${userId}/${aid}`, { method: 'POST' })
                    .then(response => response.text())
                    .then(result => {
                        alert(result);
                        location.reload();
                    })
                    .catch(error => console.error('Error setting default address:', error));
            });
        }

        addressCard.appendChild(defaultDiv);
        addressCard.appendChild(addressDetails);
        addressCard.appendChild(addressActions);
        addressContainer.appendChild(addressCard);
    }
});
