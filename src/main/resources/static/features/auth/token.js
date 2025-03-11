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
	
	getUsername: function () {
	    const token = localStorage.getItem(this.JWT_KEY);
	    if (!token) {
	        return null; // or some default value indicating no username
	    }
		
	    const payload = JSON.parse(atob(token.split('.')[1])); // Decode the token payload
	    console.log(payload);
		return payload.sub || null; // Return the username or null if it's not available
	},
	
	isQuizzer: function () {
	    const token = localStorage.getItem(this.JWT_KEY);
	    if (!token) {
	        return null; // or some default value indicating no username
	    }

	    const payload = JSON.parse(atob(token.split('.')[1]));
	    if (!payload.roles) return false;
	    return payload.roles.includes("ROLE_QUIZZER");
	},

	isQuizDesigner: function () {
	    const token = localStorage.getItem(this.JWT_KEY);
	    if (!token) {
	        return null; // or some default value indicating no username
	    }

	    const payload = JSON.parse(atob(token.split('.')[1]));
	    if (!payload.roles) return false;
	    return payload.roles.includes("ROLE_QUIZ_DESIGNER");
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