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
	},
	
	isLoggedIn: function () {
	    const token = localStorage.getItem(this.JWT_KEY);
	    if (!token) {
	        return false;
	    }
		
	    const payload = JSON.parse(atob(token.split('.')[1])); 

	    const currentTime = Math.floor(Date.now() / 1000);

	    if (payload.exp < currentTime) {
	        this.logout();
	        return false;
	    }

	    return true;
	},
	
	logout: function () {
		localStorage.removeItem(this.JWT_KEY);
	}
}