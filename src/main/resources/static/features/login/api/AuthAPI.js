const BASE_URL = "http://localhost:8081"

const AuthAPI = {
	login : function (username, password) {
		return new Promise((resolve, reject) => {
			// Make request
			$.ajax({
	            url: `${BASE_URL}/api/auth/login`,
	            type: "POST",
	            contentType: "application/json",
	            data: JSON.stringify({username, password}),
	            success: callback,
	            error: errorCallback
	        });
			
			// Save token if success
			function callback(response) {
				if (!response.token) {
					reject(new ServerSideError("No token received from backend."));
				} else {
					saveToken(response.token);
					resolve();	
				}
			}
			
			// throw error otherwise
			function errorCallback(xhr) {
				let statusCode = xhr.status;
				if (statusCode === 401) {
					reject(new InvalidCredentialsError("Invalid Username or Password."));
				} else {
					reject(new ServerSideError("Encountered unexpected error while trying to log in."));
				}
			}
		});
	},
	
	logout : function() {
		removeToken();
	}
}

// Token Storage --------
function saveToken(token) {
	localStorage.setItem("jwt", token);
}

function removeToken() {
	localStorage.removeItem("jwt");
}

// Errors -------
class InvalidCredentialsError extends Error {
	constructor(message) {
		super(message);
		this.name = "InvalidCredentialsError";
	}
}

class ServerSideError extends Error {
	constructor(message) {
		super(message);
		this.name = "ServerSideError";
	}
}
