// js/router.js
const Router = {
	paths: {
		"login": "/features/login/login.html",
		"quizzer-dashboard": "/quizzer/features/dashboard/dashboard.html",
		"quiz-developer-dashboard": "/quiz_developer/features/dashboard/dashboard.html",
		"quiz_gameplay": "/quizzer/features/quiz_gameplay/quiz_game.html"
	},
	
	initialized: false,
	init: function () {
		if (this.initialized) return;
		this.initialized = true;
		
		$(document).off("click", "[quiz-card]").on("click", "[quiz-card]", function () {
	    	const quizId = $(this).attr("id")
	        Router.navigate(`quiz_gameplay?quiz=${quizId}`);
			
	    });
		
		// Listen for URL hash changes (back/forward navigation)
	    $(window).off("hashchange").on("hashchange", function () {
	    	let page = location.hash.substring(1) || this.loginPageRef;
	    	Router.loadPage(page);
	    });
		
	    // Perform the initial page load based on the current hash (default to "login")
	    let initialPage = location.hash.substring(1) || "login";
	    Router.loadPage(initialPage);
	},

	navigate: function (page) {
    	// Update the URL hash and load the corresponding page
    	history.pushState(null, "", `#${page}`);
    	Router.loadPage(page);
	},
	
	loadDashboard: function () {
		if (TokenStorage.isQuizzer()) {
			Router.loadPage("quizzer-dashboard");
		} else if (TokenStorage.isQuizDesigner()) {
			Router.loadPage("quiz-developer-dashboard");
		}
	},
	
	loadPage: function (page) {
		// Enforce authentication: if not authenticated and page isn’t "login", force login.
		if (!TokenStorage.isLoggedIn()) {
      		page = "login"; // Redirect to login
      		history.pushState(null, "", "#login");
   		} else if (page === "login") {
			Router.loadDashboard(); // Redirect to quizzer-dashboard when you try to go to login page when already logged in
			return;
		}
		
		// -------------- PAGES ------------
		// Show or hide the navbar and sidebar based on the page.
	    if (page === "login") {
	    	$("#navbar").hide();
	    } else {
	    	$("#navbar").show();
			// Load navbar
			Router.loadContentInto('/features/navbar/navbar.html', "navbar");
	    }
		
		// Special case
		if (page.startsWith("quiz_gameplay?quiz=")) {
			Router.loadContentInto(`${this.paths["quiz_gameplay"]}`, "content");
		} else {
			Router.loadContentInto(`${this.paths[page]}`, "content");	
		}
		
		// Push state
		history.pushState(null, "", `#${page}`);
		
		// Load footer
		// Router.loadContentInto("/components/footer.html", "footer");
	},
	
	loadContentInto: function (src, idOfDest) {
		$.get(src, function(responseText) {
			const parser = new DOMParser();
			const contentDoc = parser.parseFromString(responseText, 'text/html');		
	        const bodyContent = $(contentDoc).find('body').html();
			
			// Set content to the content above
	        $(`#${idOfDest}`).html(bodyContent);

	        // If there are scripts in the loaded content, execute them
	        $(responseText).find('script').each(function() {
	            $.getScript($(this).attr('src'));
	        });
	    });
	}
};

$(document).ready(function () {
    Router.init();
});