$(document).ready(function () {
	console.log("navbar.js loaded");
	
	$("#logout").off('click').on('click', logout);
	
	function logout() {
		TokenStorage.removeToken();
		alert("Logged out!");
	}
});