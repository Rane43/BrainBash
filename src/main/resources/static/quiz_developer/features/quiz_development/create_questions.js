const QuizEditor = {
	quizId: null,
	quizTitle: null,
	quizDescription: null,
	quizImage: null,
	questionIds: null,
	ageRating: null,
	difficultyRating: null,
	category: null,
	
	currentQuestion: null,
	currentQuestionIndex: -1,
	
	init: function (quizGameDto) {
		this.quizId = quizGameDto.id;
		this.quizTitle = quizGameDto.title;
		this.quizDescription = quizGameDto.description;
		this.quizImage = quizGameDto.image;
		this.questionIds = quizGameDto.questionIds;
		this.ageRating = quizGameDto.ageRating;
		this.difficultyRating = quizGameDto.difficultyRating;
		this.category = quizGameDto.category;
		
		if (this.questionIds.length > 0) {
			this.currentQuestionIndex = 0;
			this.loadCurrentQuestion();	
		} else {
			this.addQuestion();
		}
	},
	
	loadCurrentQuestion: function () {
		const questionId = this.questionIds[this.currentQuestionIndex];
		
		$.ajax({
			url: `/api/quizzes/questions/${questionId}`,
			method: "GET",
			dataType: "json",
			success: (questionDto) => {
				this.currentQuestion = questionDto;
				this.updateDisplay();
			},
			error: () => {
				console.log("Error loading current question...");
			}
		});
	},
	
	saveQuestion() {
		// Ajax request and updateDisplay
		const answerDtos = [];
		
		$('#answer-edit-btn-group input').each(function() {
		    const text = $(this).val();
		    const correct = $("#correct-answer-dropdown").val() === $(this).attr("id");
	
		    answerDtos.push({
		      text: text,
		      correct: correct
		    });
		});
	
		const dto = {
		    text: $("#edit-question-text").val(),
		    answerRequestDtos: answerDtos
		};
		

		console.log(dto);
		
		$.ajax({
			url: `/api/quizzes/questions/${this.currentQuestion.id}`,
			method: "PUT",
			headers: {
				"Authorization": `Bearer ${TokenStorage.getToken()}`,
				"Content-Type": "application/json"
			},
			data: JSON.stringify(dto),
			success: () => {
				this.loadCurrentQuestion();
			},
			error: (response) => {
				console.log("Error updating question..." + response.status);
			}
		});
	},
	
	updateDisplay() {
        if (!this.currentQuestion) return;
		
		if (this.currentQuestionIndex < 0 || this.currentQuestionIndex >= this.questionIds.length) return;
		
		if (this.currentQuestionIndex < 1) {
			$("#prev-btn").hide();	
		} else {
			$("#prev-btn").show();
		};
		
		if (this.currentQuestionIndex >= this.questionIds.length - 1) {
			$("#next-btn").hide();	
			$("#new-question-btn").show();
		} else {
			$("#next-btn").show();
			$("#new-question-btn").hide();
		};
		
		$("#edit-question-text").attr("value", this.currentQuestion.text);
        $("#question-text").text(this.currentQuestion.text);
		
        const answerGroup = $("#answer-group").empty();
		const inputGroup = $("#answer-edit-btn-group").empty();
		const correctAnswerDropdown = document.getElementById("correct-answer-dropdown");
		correctAnswerDropdown.innerHTML = "";
        this.currentQuestion.answerDtos.forEach((answer) => {
            let btn = QuizTemplates.answerButton(answer);
			let inputAns = QuizCreationTemplates.answerInput(answer.text);
			
			addOption(correctAnswerDropdown, answer.id ,answer.text)
            
			btn.attr("id", answer.id);
			inputAns.attr("id", answer.id);
			
            answerGroup.append(btn);
			inputGroup.append(inputAns);
        });
    },
	
	test: function () {
		console.log(this.quizId);
		console.log(this.quizTitle);
		console.log(this.quizDescription);
		console.log(this.quizImage);
		console.log(this.questionIds);
		console.log(this.ageRating);
		console.log(this.difficultyRating); 
		console.log(this.category);
		console.log(this.currentQuestion);
	},
	
	prevQuestion: function () {
		if (this.currentQuestionIndex < 1) return;
		
		this.currentQuestionIndex--;
		this.loadCurrentQuestion();
	},
	
	nextQuestion: function () {
		if (this.currentQuestionIndex >= this.questionIds.length) return;
		
		this.currentQuestionIndex++;
		this.loadCurrentQuestion();
	},
	
	addQuestion: function () {
		// Default dto
		const defaultDto = {
			text: "Question title... ?",
			answerRequestDtos: [
				{
					text: "Answer 1",
					correct: true
				},
				{
					text: "Answer 2",
					correct: false
				},
				{
					text: "Answer 3",
					correct: false
				}
			]
		}
		
		$.ajax({
			url: `/api/quizzes/${this.quizId}/questions`,
			method: "POST",
			headers: {"Authorization": `Bearer ${TokenStorage.getToken()}`},
			contentType: "application/json",
			data: JSON.stringify(defaultDto),
			success: (questionId) => {
				this.questionIds.push(questionId);
				this.nextQuestion();
			},
			error: () => {
				console.log("Error creating question...");
			}
		});
	}
}

function addOption(dropdown, value, text) {
	let option = document.createElement("option");
	option.setAttribute("value", value);
	option.text=text;
	dropdown.add(option);
}



$(document).ready(function () {
	// Quiz id obtained from url
	let id = 15; // Hardcoded for development
	
	// Fetch quiz
	$.ajax({
		url: `/api/quizzes/${id}`,
		method: "GET",
		headers: {"Authorization": `Bearer ${TokenStorage.getToken()}`},
		dataType: "json",
		success: (quizGameDto) => {
			// Initialise QuizEditor
			QuizEditor.init(quizGameDto);
			QuizEditor.test();
			
			// Event Listeners
			const editBtn = $("#edit-question-btn");
			const saveBtn = $("#save-question-btn");
			const editAnswerGroup = $("#answer-edit-btn-group");
			const answerGroup = $("#answer-group");
			const titleEditInput = $("#edit-question-text");
			const title = $("#question-text");
			const correctAnswerDropdown = $("#correct-answer-div");
			
			// Edit button event listener
			editBtn.off("click").on("click", () => {
				editAnswerGroup.show();
				answerGroup.hide();
				titleEditInput.show();
				title.hide();
				correctAnswerDropdown.show();
				
				saveBtn.show();
				editBtn.hide();
			});
			// Save question event listener
			saveBtn.off("click").on("click", () => {
				QuizEditor.saveQuestion();
			
				answerGroup.show();
				editAnswerGroup.hide();
				titleEditInput.hide();
				title.show();
				correctAnswerDropdown.hide();
								
				editBtn.show();
				saveBtn.hide();
			});
			// prev question event listener
			$("#prev-btn").off("click").on("click", () => {
				QuizEditor.prevQuestion();
			});
			// next question event listener
			$("#next-btn").off("click").on("click", () => {
				QuizEditor.nextQuestion();
			});
			// New question event listener
			$("#new-question-btn").off("click").on("click", () => {
				QuizEditor.addQuestion();
			});
			
		},
		error: () => {
			console.log(`Error retrieving quiz with id: ${id}`);
		}
	});
});