$(document).ready(function() {
	console.log("login.js loaded!");
	
	// Event Listener for register button
	$().off("click").on("click", register);
	
	// Event Listener for login button
	$("#login-btn").off("click").on("click", login);
	
	
	// Event listeners for register link and log in links
	$("#register-btn").off("click").on("click", register);
	
	
	// -------------- FUNCTIONS ------------------
	// ----------------- Register ----------------
	function register() {
		let username = $("#register-username").val().trim().toLowerCase(); // Username is case-insensitive
		let password = $("#register-password").val().trim();
		let role = $("#register-role").val();
		
		console.log(username);
		console.log(password);
		console.log(role);
		
		// Validate username and password
		if (!username || !password || !role) {
			displayRegisterErrorMessage("Please supply all required fields");
			return;
		}
		
		
		// Make request
		$.ajax({
	        url: `/api/auth/register`,
	        method: "POST",
			headers: {
				"contentType": "application/json"
			},
	        contentType: "application/json",
	        data: JSON.stringify({username, password, role}),
	        success: registerSuccess,
	        error: registerFailure
	    });
		
	}
	
	function registerSuccess(response) {
		let token = response.token;
		if (!token) {
			displayRegisterErrorMessage("No token received from backend.");	
		} else {
			TokenStorage.saveToken(token);
			clearRegisterErrors();
			// Redirect to the landing page
			Router.navigate("dashboard");
		}
	}
	
	function registerFailure(xhr) {
		let statusCode = xhr.status;
		if (statusCode === 409) {
			displayRegisterErrorMessage("Username is taken.");
		} else {
			displayRegisterErrorMessage("Encountered unexpected error while trying to log in.");
		}
	}
	
	function displayRegisterErrorMessage(msg) {
		clearRegisterErrors();
		
		$("#register-error-container").show();
		$("#register-error").text(msg);
	}
	
	function clearRegisterErrors() {
		$("register-error-container").hide();
	}
	
	
	
	
	
	// ------------------ Login ----------------
	function login() {
		// Begin login process
		let username = $("#username").val().trim().toLowerCase(); // Username is case-insensitive
		let password = $("#password").val().trim();
		let role = $("#register-role").val();
		
		// Validate username and password
		if (!username || !password) {
			displayLoginErrorMessage("Username and password must not be empty.");
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
	        success: loginSucess,
	        error: loginFailure
	    });
	}
		
	// Callback functions --
	function loginSucess(response) {
		let token = response.token;
		if (!token) {
			displayLoginErrorMessage("No token received from backend.");	
		} else {
			TokenStorage.saveToken(token);
			clearLoginErrors();
			// Redirect to the landing page
			Router.navigate("dashboard");
		}
	}
	
	function loginFailure(xhr) {
		let statusCode = xhr.status;
		if (statusCode === 401) {
			displayLoginErrorMessage("Invalid Username or Password") // display error response message ?
		} else {
			displayLoginErrorMessage("Encountered unexpected error while trying to log in.");
		}
	}
	
	function displayLoginErrorMessage(errorMessage) {
		clearLoginErrors();
		
		// Display current error message
		$("#login-error-container").show();
		$("#login-error").text(errorMessage);
	}
	
	function clearLoginErrors() {
		// Clear previous error messages
		$("#login-error-container").hide();
	}
});

