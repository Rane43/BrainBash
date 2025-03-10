$(document).ready(function () {
	console.log("navbar.js loaded");
	
	$("#logout").off('click').on('click', logout);
	
	$("#brand-name").off('click').on('click', () => {
		Router.navigate('dashboard');
	});
	
	function logout() {
		TokenStorage.removeToken();
		alert("Logged out!");
		Router.loadPage('login');
	}
});