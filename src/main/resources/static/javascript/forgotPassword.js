document.addEventListener('DOMContentLoaded', () => {
    const forgotPasswordForm = document.querySelector('#forgotPasswordForm');
    const sendOtpButton = document.querySelector('#sendOtpButton');
    const otpSection = document.querySelector('#otpSection');
    const userOtpInput = document.querySelector('#userOtp');
    const submitOtpButton = document.querySelector('#submitOTP');

    // Handle Send OTP button click
    sendOtpButton.addEventListener('click', async (event) => {
        event.preventDefault();

        const email = document.querySelector('#userEmail').value;

        // Send OTP request to the server
        try {
            const response = await fetch('http://localhost:8080/send-password-reset-otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email }),
            });

            if (response.ok) {
                alert('OTP sent successfully to your email.');
                otpSection.style.display = 'flex'; // Show the OTP input and submit button
                sendOtpButton.disabled = true; // Disable Send OTP button
            } else {
                const result = await response.text();
                alert(result || 'Failed to send OTP. Please try again.');
            }
        } catch (error) {
            console.error('Error sending OTP:', error);
            alert('Error sending OTP. Please try again later.');
        }
    });

    // Handle Verify OTP button click
    submitOtpButton.addEventListener('click', async (event) => {
        event.preventDefault();
        const email = document.querySelector('#userEmail').value;
        const otp = userOtpInput.value;

        // Verify OTP with the server
        try {
            const response = await fetch('http://localhost:8080/verify-password-reset-otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ otp }),
            });

            if (response.ok) {
                alert('OTP verified successfully. You can now reset your password.');
                
                // Redirect to reset-password page with email as a query parameter
                window.location.href = `/reset-password?email=${encodeURIComponent(email)}`;
            } else {
                const result = await response.text();
                alert(result || 'Invalid OTP. Please try again.');
            }
        } catch (error) {
            console.error('Error verifying OTP:', error);
            alert('Error verifying OTP. Please try again later.');
        }
    });
});
