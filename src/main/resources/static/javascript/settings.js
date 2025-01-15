document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem("userIdCustomer");
    const apiUrl = `/get-account-preference/${userId}`;

    // Fetch account preferences
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch account preferences');
        }
        return response.json();
    })
    .then(data => {
		console.log(data);
        const enableAllCheckbox = document.getElementById('enable-all');
        const notificationFlagCheckbox = document.getElementById('notification-flag');
        const newsletterFlagCheckbox = document.getElementById('newsletter-flag');
        const promosOfferFlagCheckbox = document.getElementById('promos-offer-flag');
        const orderStatusFlagCheckbox = document.getElementById('order-status-flag');
		
		const coinsField = document.getElementById('coins');
		const preferenceIdField = document.getElementById('preferenceId');
		const userIdField = document.getElementById('userId');
		
		coinsField.value = data.coins;
		preferenceIdField.value = data.preferenceId;
		userIdField.value = data.userId;
	
        // Initialize the toggle states
        enableAllCheckbox.checked = data.notificationFlag && data.newsletterFlag && data.promosAndOfferFlag && data.orderStatusFlag;
        notificationFlagCheckbox.checked = data.notificationFlag;
        newsletterFlagCheckbox.checked = data.newsletterFlag;
        promosOfferFlagCheckbox.checked = data.promosAndOfferFlag;
        orderStatusFlagCheckbox.checked = data.orderStatusFlag;

        // Add event listener for 'enable-all' checkbox
        enableAllCheckbox.addEventListener('change', () => {
            const isChecked = enableAllCheckbox.checked;
            notificationFlagCheckbox.checked = isChecked;
            newsletterFlagCheckbox.checked = isChecked;
            promosOfferFlagCheckbox.checked = isChecked;
            orderStatusFlagCheckbox.checked = isChecked;
        });

        // Function to update 'enable-all' based on individual toggles
        const updateEnableAll = () => {
            enableAllCheckbox.checked = notificationFlagCheckbox.checked &&
                                        newsletterFlagCheckbox.checked &&
                                        promosOfferFlagCheckbox.checked &&
                                        orderStatusFlagCheckbox.checked;
        };

        // Add event listeners for individual toggles
        [notificationFlagCheckbox, newsletterFlagCheckbox, promosOfferFlagCheckbox, orderStatusFlagCheckbox].forEach(toggle => {
            toggle.addEventListener('change', updateEnableAll);
        });
		
		
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load preferences. Please try again.');
    });
	
	const saveButton = document.getElementById("save-preferences");
	saveButton.addEventListener("click", async () => {
	        const notificationFlag = document.getElementById('notification-flag').checked;
	        const newsletterFlag = document.getElementById('newsletter-flag').checked;
	        const promosOfferFlag = document.getElementById('promos-offer-flag').checked;
	        const orderStatusFlag = document.getElementById('order-status-flag').checked;
			const coinsField = document.getElementById('coins');
			const preferenceIdField = document.getElementById('preferenceId');
			const userIdField = document.getElementById('userId');

	        const accountPreference = {
	            userId: userIdField.value,
	            notificationFlag: notificationFlag,
	            newsletterFlag: newsletterFlag,
	            promosAndOfferFlag: promosOfferFlag,
	            orderStatusFlag: orderStatusFlag,
	            coins: coinsField.value,
				preferenceId : preferenceIdField.value
	        };

	        const apiUrl = '/update-account-preference';

	        try {
	            const response = await fetch(apiUrl, {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/json'
	                },
	                body: JSON.stringify(accountPreference)
	            });

	            if (!response.ok) {
	                throw new Error('Failed to save account preferences');
	            }
	            
	            const result = await response.json();
	            if (result) {
	                alert('Preferences updated successfully.');
	            } else {
	                alert('Failed to update preferences. Please try again.');
	            }
	        } catch (error) {
	            console.error('Error:', error);
	            alert('An error occurred while saving preferences.');
	        }
	    });

});
