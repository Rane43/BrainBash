$(document).ready(function() {
	console.log("login.js loaded!");
	
	// Event Listener for login button
	$("#login-btn").off("click").on("click", login);
	
	
	// FUNCTIONS ---
	function login() {
		// Begin login process
		let username = $("#username").val().trim().toLowerCase(); // Username is case-insensitive
		let password = $("#password").val().trim();
		
		// Validate username and password
		if (!username || !password) {
			displayErrorMessage("Username and password must not be empty.");
			return;
		}
		
		// Make request
		$.ajax({
	        url: `/api/auth/login`,
	        method: "POST",
			headers: {
				"contentType": "application/json"
			},
	        contentType: "application/json",
	        data: JSON.stringify({username, password}),
	        success: success,
	        error: failure
	    });
	}
		
	// Callback functions --
	function success(response) {
		let token = response.token;
		if (!token) {
			displayErrorMessage("No token received from backend.");	
		} else {
			TokenStorage.saveToken(token);
			clearErrors();
			// Redirect to the landing page
			Router.navigate("dashboard");
		}
	}
	
	function failure(xhr) {
		let statusCode = xhr.status;
		if (statusCode === 401) {
			displayErrorMessage("Invalid Username or Password") // display error response message ?
		} else {
			reject(new ServerSideError("Encountered unexpected error while trying to log in."));
		}
	}
	
	function displayErrorMessage(errorMessage) {
		clearErrors();
		
		// Display current error message
		$("#login-error-container").show();
		$("#login-error").text(errorMessage);
	}
	
	function clearErrors() {
		// Clear previous error messages
		$("#login-error-container").hide();
	}
});

