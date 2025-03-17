$(document).ready(function () {
	
	setupQuizDeveloperDashboard();
	
	
	$("#search-bar").on("keyup", function () {
	    displayQuizzesBySearch();
	});
	
	$("#age-rating-dropdown").on("change", function () {
		displayQuizzesBySearch();
	});
	
	$("#difficulty-rating-dropdown").on("change", function () {
		displayQuizzesBySearch();
	})
	
	
	/* Create quiz functionality */
	$("#create-quiz-button").off("click").on("click", showQuizCreationModal);
	
	$("#submit-quiz-creation").off("click").on("click", createQuiz);
});

function setupQuizDeveloperDashboard() {
	$.ajax({
		url: `/api/quizzes/mine/search`, // Search for all quizzes
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`,
			"Accept": "application/hal+json"
		},
		dataType: "json",
		success: (response) => {
			// Embed URLs
			$("#submit-quiz-creation").attr("href", response._links.createQuiz.href);
			 
			// URLs for modal
			$("#modal-category-dropdown").attr("href", response._links.categories.href);
			$("#modal-age-rating-dropdown").attr("href", response._links.ageRatings.href);
			$("#modal-difficulty-rating-dropdown").attr("href", response._links.difficultyRatings.href);
			 
			// URLs for Filters
			$("#age-rating-dropdown").attr("href", response._links.ageRatings.href);
			$("#difficulty-rating-dropdown").attr("href", response._links.difficultyRatings.href);
			$("#quiz-creation-carousel").attr("href", response._links.quizImages.href);
			
			/* Search Functionality */
			Promise.all([populateAgeRatingFilter(), populateDifficultyRatingFilter()]).then(() => {
				// Display quizzes
				if (!response._embedded) return;
				displayQuizzes(response._embedded.quizSummaryDtoList);
			});
		},
		error: () => {
			console.log("Error retrieving quizzes...");
		}
	});
}


/* ------------------- SEARCH FUNCTIONALITY ------------------ */
function displayQuizzesBySearch() {
	let searchText = $("#search-bar").val().trim();
	let ageRating = $("#age-rating-dropdown").val();
	let difficultyRating = $("#difficulty-rating-dropdown").val();

	$.ajax({
		url: `/api/quizzes/mine/search?middleTitle=${searchText}&difficultyRating=${difficultyRating}&ageRating=${ageRating}`,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`
		},
		dataType: "json",
		success: (response) => {
			displayQuizzes(response._embedded.quizSummaryDtoList);
		},
		error: () => {
			console.log("Error retrieving quizzes...");
		}
	});
}

// --------------------- FETCHING DATA -----------------------
function addOption(dropdown, value, text) {
	let option = document.createElement("option");
	option.setAttribute("value", value);
	option.text=text;
	dropdown.add(option);
}

/* AGE RATINGS */
function fetchAgeRatings(successFunc, errorFunc) {
	const url = $("#modal-age-rating-dropdown").attr("href");
	$.ajax({
		url: url,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`	
		},
		dataType: "json",
		success: (ageRatings) => {
			successFunc(ageRatings);
		}, 
		error: () => {
			errorFunc();
		}
	});
}

/* DIFFICULTY RATINGS */
function fetchDifficultyRatings(successFunc, errorFunc) {
	const url = $("#modal-difficulty-rating-dropdown").attr("href");
	$.ajax({
		url: url,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`	
		},
		dataType: "json",
		success: (difficultyRatings) => {						
			successFunc(difficultyRatings);
		}, 
		error: () => {
			errorFunc();
		}
	});
}

/* CATEGORIES */
function fetchCategories(successFunc, errorFunc) {
	const url = $("#modal-category-dropdown").attr("href");
	$.ajax({
		url: url,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`	
		},
		dataType: "json",
		success: (categories) => {
			successFunc(categories);
		}, 
		error: () => {
			errorFunc();
		}
	});
}

/* IMAGES */
function fetchImages(successFunc, errorFunc) {
	const url = $("#quiz-creation-carousel").attr("href");
	$.ajax({
		url: url,
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`	
		},
		success: (imageNames) => {
			successFunc(imageNames);
		},
		error: () => {
			errorFunc();
		}
	});
}

/* POPULATE FILTERS */
function populateAgeRatingFilter() {
	return new Promise((resolve, reject) => {
		fetchAgeRatings((ageRatings) => {
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
		() => {
			console.log("Error retrieving ");
			reject();
		});
	});
}

function populateDifficultyRatingFilter() {
	return new Promise((resolve, reject) => {
		fetchDifficultyRatings((difficultyRatings) => {
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
		() => {
			console.log("Error retrieving difficulty ratings..");
			reject();
		});
	});
}



// ------------------ DISPLAY QUIZZES ------------------
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
		quizCards.forEach(card => {
			card.attr("quiz-editor-card", "");
		});
		
        $("#quiz-display").append(CardTemplates.createCategorySlider(category, quizCards));
    }
}


/* ---------------- QUIZ CREATION ---------------- */
function createQuiz() {
	clearErrorMessage();
	
	const image = $('#quiz-creation-carousel .carousel-item.active img').attr('image-name');
    const title = $("#quiz-creation-title").val().trim();
    const description = $("#quiz-description").val().trim();
    const ageRating = $("#modal-age-rating-dropdown").val().trim();
    const difficultyRating = $("#modal-difficulty-rating-dropdown").val().trim();
    const category = $("#modal-category-dropdown").val().trim();
    
    // Create an object to pass to the API
    const quizCreationDto = {
        title: title,
        description: description,
        image: image,
        ageRating: ageRating,
        difficultyRating: difficultyRating,
        category: category
    };
    
    try {
        validateQuizCreationRequest(quizCreationDto);
	} catch (error) {
		
		errorMessage(error.message);
		return;
	}
	
	const url = $("#submit-quiz-creation").attr("href");
    $.ajax({
        url: url,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(quizCreationDto),
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`
		},
        dataType: "json",
        success: (response) => {
			displayQuizzesBySearch();
			$("#quiz-modal").modal('hide');
        },
        error: (xhr, status, error) => {
            errorMessage("An error occurred while creating the quiz. Please try again.");
        }
    });
}

function errorMessage(message) {
	$("#create-quiz-error").text(message);
	$("#create-quiz-error-container").show();
}

function clearErrorMessage() {
	$("#create-quiz-error-container").hide();
	$("#create-quiz-error").text("");
}

function validateQuizCreationRequest(quizCreationObj) {
    const MAX_TITLE_LENGTH = 50;
    const MAX_DESCRIPTION_LENGTH = 100;

    if (!quizCreationObj.title || !quizCreationObj.description || !quizCreationObj.image || 
        !quizCreationObj.ageRating || !quizCreationObj.difficultyRating || !quizCreationObj.category) {
        throw new Error("All fields are required.");
    }

    if (quizCreationObj.title.length > MAX_TITLE_LENGTH) {
        throw new Error(`Title cannot exceed ${MAX_TITLE_LENGTH} characters.`);
    }

    if (quizCreationObj.description.length > MAX_DESCRIPTION_LENGTH) {
        throw new Error(`Description cannot exceed ${MAX_DESCRIPTION_LENGTH} characters.`);
    }
}




/* ---------------- POPULATE QUIZ CREATION MODAL ------------- */
function showQuizCreationModal() {
	// Populate modal
	Promise.all([
		populateModalCategory(), 
		populateModalAgeRatings(),
		populateModalDifficultyRatings(),
		populateModalImages()
	]).then(() => {
		$("#quiz-modal").modal('show');
	});
	
}

function populateModalAgeRatings() {
	return new Promise((resolve, reject) => {
		fetchAgeRatings((ageRatings) => {
			// Populate Difficulty Ratings
			let modalAgeRatingsDropdown = document.getElementById("modal-age-rating-dropdown");
			modalAgeRatingsDropdown.innerHTML = "";
			
			ageRatings.forEach((ageRating) => {
				addOption(modalAgeRatingsDropdown, ageRating, ageRating);
			});
			resolve();
		},
		() => {
			console.log("Error retrieving age ratings for modal..");
			reject();
		});
	});
}

function populateModalDifficultyRatings() {
	return new Promise((resolve, reject) => {
		fetchDifficultyRatings((difficultyRatings) => {
			// Populate Difficulty Ratings
			let modalDifficultyRatingDropdown = document.getElementById("modal-difficulty-rating-dropdown");
			modalDifficultyRatingDropdown.innerHTML = "";
			
			difficultyRatings.forEach((difficultyRating) => {
				addOption(modalDifficultyRatingDropdown, difficultyRating, difficultyRating);
			});
			resolve();
		},
		() => {
			console.log("Error retrieving difficulty ratings for modal..");
			reject();
		});
	});
}

function populateModalCategory() {
	// Populate Categories
	return new Promise((resolve, reject) => {
		fetchCategories((categories) => {
			// Populate Difficulty Ratings
			let modalCategoryDropdown = document.getElementById("modal-category-dropdown");
			modalCategoryDropdown.innerHTML = "";
			
			categories.forEach((category) => {
				addOption(modalCategoryDropdown, category, category);
			});
			resolve();
		},
		() => {
			console.log("Error retrieving categories for modal..");
			reject();
		});
	});	
}

function populateModalImages() {
	// Populate Images
	return new Promise((resolve, reject) => {
		fetchImages((imageNames) => {
			// Clear previous indicators and images
			$("#quiz-creation-carousel").empty();
			$("#image-indicators").empty();
			
			// Populate Images
			let i = 0;
			imageNames.forEach((imageName) => {
				let imageDiv = QuizCreationTemplates.quizCreationImage(imageName);
				let imageIndicator = QuizCreationTemplates.imageIndicatorQuizCreation(i);
				if (i === 0) {
					imageDiv.addClass("active");
					imageIndicator.addClass("active");
				}
				$("#quiz-creation-carousel").append(imageDiv);
				$("#image-indicators").append(imageIndicator);
				i++;
			});
			
			resolve();
		},
		() => {
			console.log("Error retrieving images for modal..");
			reject();
		});
	});	
}


