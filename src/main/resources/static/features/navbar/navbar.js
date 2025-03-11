$(document).ready(function () {
	console.log("navbar.js loaded");
	
	$("#logout").off('click').on('click', logout);
	
	$("#brand-name").off('click').on('click', () => {
		Router.navigate('dashboard');
	});
	
	let username = TokenStorage.getUsername();
	console.log(username)
	$("#user-name").text(username);
	
	function logout() {
		TokenStorage.removeToken();
		Router.loadPage('login');
	}
});