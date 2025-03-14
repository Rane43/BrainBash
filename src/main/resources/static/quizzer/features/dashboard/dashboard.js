$(document).ready(function () {
	fetchQuizzes();
	fetchAgeRatings();
	fetchDifficultyRatings();
	
	$("#search-bar").on("keyup", function () {
	    displayQuizzesBySearch();
	});
	
	$("#age-rating-dropdown").on("change", function () {
		displayQuizzesBySearch();
	});
	
	$("#difficulty-rating-dropdown").on("change", function () {
		displayQuizzesBySearch();
	})
});



function displayQuizzesBySearch() {
	let searchText = $("#search-bar").val().trim();
	let ageRating = $("#age-rating-dropdown").val();
	let difficultyRating = $("#difficulty-rating-dropdown").val();
	
	fetchQuizzesBySearch(searchText, difficultyRating, ageRating);	
}

function fetchQuizzesBySearch(searchText, difficultyRating, ageRating) {
	$.ajax({
		url: `/api/quizzes/search?middleTitle=${searchText}&difficultyRating=${difficultyRating}&ageRating=${ageRating}`,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`
		},
		dataType: "json",
		success: (quizSummaryDtos) => {
			displayQuizzes(quizSummaryDtos);
		},
		error: () => {
			console.log("Error retrieving quizzes...");
		}
	});
}

// Fetch Queries options
/* AGE RATING */
function fetchAgeRatings() {
	$.ajax({
		url: "/api/age-ratings",
		method: "GET",
		dataType: "json",
		success: (ageRatings) => {
			// Populate Categories
			let ageRatingDropdown = document.getElementById("age-rating-dropdown");
			ageRatingDropdown.innerHTML = "";
			
			// Default 'All Age Ratings'
			addOption(ageRatingDropdown, "", "All Age Ratings");
			ageRatings.forEach((ageRating) => {
				addOption(ageRatingDropdown, ageRating, ageRating);
			});
		}, 
		error: () => {
			console.log("Error retrieving categories");
		}
	});
}

function addOption(dropdown, value, text) {
	let option = document.createElement("option");
	option.setAttribute("value", value);
	option.text=text;
	dropdown.add(option);
}


/* DIFFICULTY RATING */
function fetchDifficultyRatings() {
	$.ajax({
		url: "/api/difficulty-ratings",
		method: "GET",
		dataType: "json",
		success: (difficultyRatings) => {
			// Populate Categories
			let selectDropdown = document.getElementById("difficulty-rating-dropdown");
			selectDropdown.innerHTML = "";
			
			difficultyRatings.forEach((difficultyRating) => {
				let option = document.createElement("option");
				option.setAttribute("value", difficultyRating);
				option.text=difficultyRating;
				selectDropdown.add(option);
			});
		}, 
		error: () => {
			console.log("Error retrieving categories");
		}
	});
}



// --------- Fetch queries -----------
function fetchQuizzes() {
	$.ajax({
		url: "/api/quizzes",
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`
		},
		dataType: "json",
		success: (quizSummaryDtos) => {
			displayQuizzes(quizSummaryDtos);
		},
		error: () => {
			console.log("Error retrieving quizzes...");
		}
	});
}

function displayQuizzes(quizSummaryDtos) {
	// Empty previous content
	$("#quiz-display").empty();
	
	// Display new content
	const groupedQuizzes = quizSummaryDtos.reduce((acc, quiz) => {
        const category = quiz.category; // assuming quiz has a 'category' field
        if (!acc[category]) {
            acc[category] = [];
        }
        acc[category].push(quiz);
        return acc;
    }, {});
	
	for (const category in groupedQuizzes) {
        const quizzes = groupedQuizzes[category];
		// Collect quiz cards created from quizzes
		const quizCards = quizzes.map(CardTemplates.createQuizCard);
		
        $("#quiz-display").append(CardTemplates.createCategorySlider(category, quizCards));
    }
}
