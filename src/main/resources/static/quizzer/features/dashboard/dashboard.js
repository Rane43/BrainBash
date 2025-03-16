$(document).ready(function () {
	Promise.all([fetchAgeRatings(), fetchDifficultyRatings()]).then(displayQuizzesBySearch);
	
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
/* AGE RATINGS */
function addOption(dropdown, value, text) {
	let option = document.createElement("option");
	option.setAttribute("value", value);
	option.text=text;
	dropdown.add(option);
}

function fetchAgeRatings() {
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "/api/age-ratings",
			method: "GET",
			dataType: "json",
			success: (ageRatings) => {
				// Populate Age Ratings
				let ageRatingDropdown = document.getElementById("age-rating-dropdown");
				ageRatingDropdown.innerHTML = "";
				
				// Default 'All Age Ratings'
				addOption(ageRatingDropdown, "", "All Age Ratings");
				ageRatings.forEach((ageRating) => {
					addOption(ageRatingDropdown, ageRating, ageRating);
				});
				
				resolve();
			}, 
			error: () => {
				console.log("Error retrieving categories");
				reject();
			}
		});
	})
}

/* DIFFICULTY RATINGS */
function fetchDifficultyRatings() {
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "/api/difficulty-ratings",
			method: "GET",
			dataType: "json",
			success: (difficultyRatings) => {						
				// Populate Difficulty Ratings
				let difficultyRatingDropdown = document.getElementById("difficulty-rating-dropdown");
				difficultyRatingDropdown.innerHTML = "";
				
				// Default 'All Difficulty Ratings'
				addOption(difficultyRatingDropdown, "", "All Difficulty Ratings");
				difficultyRatings.forEach((difficultyRating) => {
					addOption(difficultyRatingDropdown, difficultyRating, difficultyRating);
				});
				
				resolve();
			}, 
			error: () => {
				console.log("Error retrieving categories");
				reject();
			}
		});
	});
}



// --------- Fetch queries -----------
function displayQuizzes(quizSummaryDtos) {
	// Empty previous content
	$("#quiz-display").empty();
	
	console.log("quiz developer display quizzes...");
	
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
		quizCards.forEach(card => {
			card.attr("quiz-card", "");
		});
		
        $("#quiz-display").append(CardTemplates.createCategorySlider(category, quizCards));
    }
}
