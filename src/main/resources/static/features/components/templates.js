// ----------------- QUIZ CARD TEMPLATES -------------------
const CardTemplates = {
	createQuizCard: function (quiz) {
		let card = $("<div>")
					.addClass("card")
					.attr("id", quiz.id)
					.attr("quiz-card", "");

		let img = $("<img>")
			.addClass("card-img-top")
			.attr("src", "/assets/images/quiz_images/" + encodeURIComponent(quiz.image))
			.attr("alt", "Quiz Image");

		let cardBody = $("<div>").addClass("card-body");

		let title = $("<h5>").addClass("card-title").text(quiz.title);
		let description = $("<p>").addClass("card-text").text(quiz.description);

		cardBody.append(title, description);
		card.append(img, cardBody);
		
		// Event Listener to open and play the quiz
		return card;
	},

	createCategorySlider: function (categoryTitle, quizCards) {
		let slider = $("<div>").addClass("category-slider");
		let title = $("<h3>").addClass("category-title").text(categoryTitle);

		let container = $("<div>").addClass("card-container");
		quizCards.forEach((quizCard) => {
			container.append(quizCard);
		});

		slider.append(title, container);
		return slider;
	}
}


// ------------------- QUIZ BUTTON TEMPLATES -------------------
const QuizTemplates = {
    answerButton: function (answer) {
        return $("<button>")
            .attr("type", "button")
            .addClass("question-btn question-answer-btn")
            .text(answer.text);
    }
};


// ------------------ QUIZ CREATION TEMPLATES ------------------
const QuizCreationTemplates = {
	quizCreationImage: function (imageName) {
		const div = $('<div>').addClass('carousel-item w-100 h-100');
		const img = $('<img>')
			.addClass('d-block w-100 h-100')
			.attr('src', `/assets/images/quiz_images/${encodeURIComponent(imageName)}`)
			.attr('image-name', imageName);

		div.append(img);
		return div;
	},
	
	imageIndicatorQuizCreation: function (num) {
		return $('<li>')
			.attr('data-target', '#carouselExampleIndicators')
			.attr('data-slide-to',`${num}`);
	}
};






