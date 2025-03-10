// ----- TEMPLATES -----
const CardTemplates = {
	createQuizCard: function (quiz) {
		let card = $("<div>").addClass("card");

		let img = $("<img>")
			.addClass("card-img-top")
			.attr("src", "/assets/images/quiz_images/" + encodeURIComponent(quiz.image))
			.attr("alt", "Quiz Image");

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
