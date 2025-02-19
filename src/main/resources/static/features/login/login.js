$(document).ready(function() {
	console.log("login.js loaded!");
	
	// Clear any errors
	clearErrors();
	
	// Event Listener for login button
	$("#login-btn").on("click", login);
});

function login() {
	// Clear previous error messages
	clearErrors();
	
	// Begin login process
	let username = $("#username").val().trim();
	let password = $("#password").val().trim();
	
	// Validate username and password
	if (!username || !password) {
		displayError("Username and password must not be empty.");
		return;
	}
	
	// Attempt to login - Display error otherwise
	AuthAPI.login(username, password)
	.then(() => {
		// Redirect
		console.log("Login successful!");
	})
	.catch((error) => {
		displayError(error.message);
	});
}

function displayError(errorMessage) {
	$("#login-error-container").show();
	$("#login-error").text(errorMessage);
}

function clearErrors() {
	$("#login-error-container").hide();
}