const TokenStorage = {
	JWT_KEY: "JWT",
	
	saveToken: function (token) {
		localStorage.setItem(this.JWT_KEY, token);
	},
	
	getToken: function () {
		return localStorage.getItem(this.JWT_KEY);
	},
	
	removeToken: function () {
		return localStorage.removeItem(this.JWT_KEY);
	}
}