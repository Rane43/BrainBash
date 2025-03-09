$(document).ready(function () {
	// Dummy question for debugging
	let question = {
		text: "Where are you from?",
		image: "/assets/images/question-image-test.jpeg",
		answers: [
			{
				text: "Ireland",
				correct: true
			},
			{
				text: "England",
				correct: false
			},
			{
				text: "France",
				correct: false
			},
			{
				text: "Germany",
				correct: false
			}
		]
	};
	
	populateQuestion(question);
});

function answerQuestion() {
	
}


function displayQuiz() {
	
}
		
function populateQuestion(question) {
	// Empty previous question answers
	$("#answer-btn-group").html("");
	
	// Title
	$("#question-question-title h1").text(question.text);
	
	// Background image in card upper
	$("#question-image").attr("src", `${question.image}`);
	
	// Answers
	question.answers.forEach(answer => {
		$("#answer-btn-group").append(QuizTemplates.answerButton(answer));
	});
}


const QuizTemplates = {
	answerButton: function (answer) {
		return	$("<button>")
				.attr("type", "button")
				.addClass("question-btn question-answer-btn")
				.text(answer.text);
	}
}