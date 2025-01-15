document.addEventListener('DOMContentLoaded', () => {
    const resetPasswordForm = document.querySelector('#resetPasswordForm');
    const resetPasswordButton = document.querySelector('#resetPasswordButton');

    resetPasswordForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.querySelector('#resetUserEmail').value;
        const newPassword = document.querySelector('#newPassword').value;
        const confirmPassword = document.querySelector('#confirmPassword').value;

        if (newPassword !== confirmPassword) {
            alert('Passwords do not match. Please try again.');
            return;
        }
		console.log(email);
        // Make the POST request to reset the password
        try {
            const response = await fetch('http://localhost:8080/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password: newPassword }),
            });

            if (response.ok) {
                const result = await response.text();
                alert(result); // Success message from the server
                window.location.href = '/login'; // Redirect to login page
            } else {
                const result = await response.text();
                alert(result || 'Failed to reset password. Please try again.');
            }
        } catch (error) {
            console.error('Error resetting password:', error);
            alert('An error occurred. Please try again later.');
        }
    });
});
