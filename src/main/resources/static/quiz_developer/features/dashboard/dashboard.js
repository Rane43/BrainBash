$(document).ready(function () {
	fetchMyQuizzes();
	
	// Event listener for quiz creation
	$("#create-quiz-button").off("click").on("click", () => {
		populateQuizCreationModal();
		$("#quiz-modal").modal('show');
	});
	
	$("#submit-quiz-creation").off("click").on("click", createQuiz);
});


function createQuiz() {
    let image = "background-image.webp";
    let title = $("#quiz-creation-title").val().trim();
    let description = $("#quiz-description").val().trim();
    let ageRating = $("#age-rating-dropdown").val().trim();
    let difficultyRating = $("#difficulty-rating-dropdown").val().trim();
    let category = $("#category-dropdown").val().trim();
    
    // Create an object to pass to the API
    let quizCreationDto = {
        title: title,
        description: description,
        image: image,
        ageRating: ageRating,
        difficultyRating: difficultyRating,
        category: category
    };
    
    try {
        validateQuizCreationRequest(quizCreationDto);
        $.ajax({
            url: `/api/quizzes`,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(quizCreationDto),
			headers: {"Authorization": `Bearer ${TokenStorage.getToken()}`},
            dataType: "json",
            success: (response) => {
				fetchMyQuizzes();
				$("#quiz-modal").modal('hide');
            },
            error: (xhr, status, error) => {
                alert("An error occurred while creating the quiz. Please try again.");
            }
        });
    } catch (error) {
        alert(error.message);
    }
}

function validateQuizCreationRequest(quizCreationObj) {
    if (!quizCreationObj.title || !quizCreationObj.description || !quizCreationObj.image || 
        !quizCreationObj.ageRating || !quizCreationObj.difficultyRating || !quizCreationObj.category) {
        throw new Error("All fields are required.");
    }
}




/* POPULATE QUIZ */
function populateQuizCreationModal() {
	// Populate modal
	fetchCategories();
	fetchAgeRatings();
	fetchDifficultyRatings();
	
}

/* CATEGORIES */
function fetchCategories() {
	$.ajax({
		url: "/api/categories",
		method: "GET",
		dataType: "json",
		success: (categories) => {
			// Populate Categories
			let selectDropdown = document.getElementById("category-dropdown");
			selectDropdown.innerHTML = "";
			
			categories.forEach((category) => {
				let option = document.createElement("option");
				option.setAttribute("value", category);
				option.text=category;
				selectDropdown.add(option);
			});
		}, 
		error: () => {
			console.log("Error retrieving categories");
		}
	});
}

/* AGE RATING */
function fetchAgeRatings() {
	$.ajax({
		url: "/api/age-ratings",
		method: "GET",
		dataType: "json",
		success: (ageRatings) => {
			// Populate Categories
			let selectDropdown = document.getElementById("age-rating-dropdown");
			selectDropdown.innerHTML = "";
			
			ageRatings.forEach((ageRating) => {
				let option = document.createElement("option");
				option.setAttribute("value", ageRating);
				option.text=ageRating;
				selectDropdown.add(option);
			});
		}, 
		error: () => {
			console.log("Error retrieving categories");
		}
	});
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





// ----------------------- QUIZ DISPLAY ------------------------
function fetchMyQuizzes() {
	$.ajax({
		url: "/api/quizzes/mine",
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
	// Clear previous content
	$("#quiz-display").empty();
	
	// Add new content
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

