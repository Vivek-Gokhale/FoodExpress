document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.querySelector('#signupform');
    const signupButton = document.querySelector('#signupButton');
    const otpContainer = document.querySelector('#otp-container');
    const otpField = document.querySelector('#otpField');
    const verifyOtpButton = document.querySelector('#verifyOtpButton');

    // Handle signup form submission
    signupForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const firstName = document.getElementById('FirstName').value;
        const lastName = document.getElementById('LastName').value;
        const phoneNo = document.getElementById('Phone_no').value;

        try {
            // Send signup data to the server
            const response = await fetch('http://localhost:8080/register-customer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password, firstName, lastName, phoneNo })
            });

            if (response.ok) {
                alert('An OTP has been sent to your email.');
                otpContainer.style.display = 'flex'; // Show OTP field and button
                signupButton.disabled = true; // Disable the signup button to prevent resubmission
            } else {
                const result = await response.json();
                alert(result.error || 'Sign up failed');
            }
        } catch (error) {
            console.error('Error during signup:', error);
            alert('An error occurred while processing your request.');
        }
    });

    // Handle OTP verification
    verifyOtpButton.addEventListener('click', async () => {
        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const firstName = document.getElementById('FirstName').value;
        const lastName = document.getElementById('LastName').value;
        const phoneNo = document.getElementById('Phone_no').value;
        const otp = otpField.value;

        try {
            // Send OTP to the server for verification
            const response = await fetch('http://localhost:8080/verify-registration-otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password, firstName, lastName, phoneNo, otp })
            });

            if (response.ok) {
                alert('OTP verified successfully. Registration complete.');
                window.location.href = '/customer-login'; // Redirect to login page
            } else {
                const result = await response.json();
                alert(result.error || 'Invalid OTP');
            }
        } catch (error) {
            console.error('Error during OTP verification:', error);
            alert('An error occurred while verifying the OTP.');
        }
    });
});
