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
	}
	
	let quizzes = [quiz1, quiz2];
	$("#content").html(CardTemplates.createCategorySlider("Geography", quizzes));
}




const CardTemplates = {
	createQuizCard: function (quiz) {
		return `<div class="card">
					<img class="card-img-top" src="/assets/images/${quiz.image}" alt="${quiz.image}">
					<div class="card-body">
				    	<h5 class="card-title">${quiz.title}</h5>
				    	<p class="card-text">${quiz.description}</p>
					</div>
				</div>`;
	},
	
	createCategorySlider: function (categoryTitle, quizzes) {
		let output = `<div class="category-slider">
						<h1 class="category-title">${categoryTitle}</h1>
				    	<div class="card-container">`
				    		
		quizzes.forEach(quiz => {
			output += this.createQuizCard(quiz) + "\n";
		})
		output += `		</div>
				  	  </div>`;
		
		return output;
	}
}