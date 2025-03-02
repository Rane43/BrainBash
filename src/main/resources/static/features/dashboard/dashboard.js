$(document).ready(function () {
	populateGeography();
});


function populateGeography() {
	let quiz1 = {
		image: "background-image.webp",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's content."
	};

	let quiz2 = {
		image: "quiz-image-test.jpeg",
		title: "NEW QUIZ!",
		description: "Some quick example text to build on the card title and make up the bulk of the card's content."
	};

	let quizzes = [quiz1, quiz2];

	$("#content").empty().append(CardTemplates.createCategorySlider("Geography", quizzes));
}


const CardTemplates = {
	createQuizCard: function (quiz) {
		let card = $("<div>").addClass("card");

		let img = $("<img>")
			.addClass("card-img-top")
			.attr("src", "/assets/images/" + encodeURIComponent(quiz.image))
			.attr("alt", "Quiz Image"); // Avoid using user input in alt to prevent injection

		let cardBody = $("<div>").addClass("card-body");

		let title = $("<h5>").addClass("card-title").text(quiz.title);
		let description = $("<p>").addClass("card-text").text(quiz.description);

		cardBody.append(title, description);
		card.append(img, cardBody);

		return card;
	},

	createCategorySlider: function (categoryTitle, quizzes) {
		let slider = $("<div>").addClass("category-slider");
		let title = $("<h1>").addClass("category-title").text(categoryTitle);

		let container = $("<div>").addClass("card-container");

		quizzes.forEach(quiz => {
			container.append(this.createQuizCard(quiz));
		});

		slider.append(title, container);
		return slider;
	}
};
