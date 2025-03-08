$(document).ready(function () {
	clearContent();
	populateGeography();
	populateHistory();
	populateScience();
	populateSport();
});


function clearContent() {
	$("#dashboard-dashboard-content").empty();
}


function populateGeography() {
	let quiz1 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz2 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};
	let quiz3 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz4 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz5 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz6 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};


	let quizzes = [quiz1, quiz2, quiz3, quiz4, quiz5, quiz6];

	$("#dashboard-content").append(CardTemplates.createCategorySlider("Geography", quizzes));
}

function populateHistory() {
	let quiz1 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz2 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quizzes = [quiz1, quiz2];

	$("#dashboard-content").append(CardTemplates.createCategorySlider("History", quizzes));
}

function populateScience() {
	let quiz1 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz2 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quizzes = [quiz1, quiz2];

	$("#dashboard-content").append(CardTemplates.createCategorySlider("Science", quizzes));
}


function populateSport() {
	let quiz1 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quiz2 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's dashboard-dashboard-content."
	};

	let quizzes = [quiz1, quiz2];

	$("#dashboard-content").append(CardTemplates.createCategorySlider("Sport", quizzes));
}
