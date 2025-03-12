$(document).ready(function () {
	$("#logout").off('click').on('click', logout);
	
	$("#brand-name").off('click').on('click', () => {
		Router.loadDashboard();
	});
	
	let username = TokenStorage.getUsername();
	$("#user-name").text(username);
	
	function logout() {
		TokenStorage.removeToken();
		Router.loadPage('login');
	}
});
