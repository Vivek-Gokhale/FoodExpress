document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('#loginform');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await fetch('http://localhost:8080/verify-customer-login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            });

            const result = await response.json();

            if (response.ok && result.status === "success") {
                alert('Login successful');
                sessionStorage.setItem('isLoggedIn', 'true');
                sessionStorage.setItem('userEmail', email);

                // Redirect to the specified URL or fallback
                const redirectTo = sessionStorage.getItem('redirectAfterLogin') || result.redirectTo || '/';
                window.location.href = redirectTo;
            } else {
                alert(result.error || 'Login failed');
            }
        } catch (error) {
            console.error('An error occurred:', error);
            alert('An error occurred while processing your request.');
        }
    });
});