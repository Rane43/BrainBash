$(document).ready(function () {
	const QuestionTracker = {
		quizId: null,
		questionIds: [],
		currentQuestionIndex: null,
		currentQuestion: null,
		
		init(quizDto) {
			this.quizId = quizDto.id;
	        this.questionIds = quizDto.questionIds;
	        this.currentQuestionIndex = 0;
		},
		
		loadQuestion: function() {
			let index = this.currentQuestionIndex;
			if (!index) return;	
			if (index < 0 || index >= questionIds.length) return;
			
			$.ajax({
	            url: `/api/questions/${questionId}`,
	            method: "GET",
	            dataType: "json",
	            success: (questionDto) => {
	                this.currentQuestion = questionDto;
	                this.displayQuestion();
	            },
	            error: () => {
	                console.error(`Error retrieving question with id ${questionId}`);
	            }
	        });
		},
		
		displayQuestion() {
	        if (!this.currentQuestion) return;

	        $("#question-text").text(this.currentQuestion.text);
	        const answerGroup = $("#answer-btn-group").empty();

	        this.currentQuestion.answerDtos.forEach((answer) => {
	            let btn = QuizTemplates.answerButton(answer);
	            btn.attr("id", answer.id);
	            btn.on("click", () => {
					// Select answer 
					this.selectedAnswerId = btn.attr("id");
					// Disable all buttons after selection
					$("#answer-btn-group button").prop("disabled", true);
					// Submit Answer
	                this.submitAnswer();
	            });
	            answerGroup.append(btn);
	        });
	    },
		
		prevQuestion: function () {
			if (this.currentQuestionIndex === 0) return;
			
			this.currentQuestionIndex--;
			this.loadQuestion();
		},
		
		/*
		* Updates question
		*/
		saveQuestion: function () {
			if (!this.currentQuestion) return;
			
		    let questionText = $("#questionText").val().trim();
			
		    let answer1 = $("#answer1").val().trim();
		    let answer2 = $("#answer2").val().trim();
		    let answer3 = $("#answer3").val().trim();
		    let answer4 = $("#answer4").val().trim();
		    
		    let correctAnswerIndex = $("#answer").val();
		    
		    let answers = [
		        { text: answer1, correct: correctAnswerIndex == 1 },
		        { text: answer2, correct: correctAnswerIndex == 2 },
		        { text: answer3, correct: correctAnswerIndex == 3 },
		        { text: answer4, correct: correctAnswerIndex == 4 }
		    ];

		    let createQuestionDto = {
		        text: questionText,
		        answerDtos: answers
		    };

		    $.ajax({
		        url: `/api/quizzes/${this.quizId}/questions/`,
		        method: "POST",
				headers: {"Authorization": `Bearer ${TokenStorage.getToken()}`},
		        contentType: "application/json",
		        data: JSON.stringify(createQuestionDto),
		        dataType: "json",
		        success: (response) => {
		            alert("Question saved successfully! ID: " + response);
		        },
		        error: (xhr) => {
		            alert("Error saving question: " + xhr.responseText);
		        }
		    });
		},
		
		nextQuestion: function () {
			if (this.currentQuestionIndex === 0) return;
				
			this.currentQuestionIndex++;
			this.loadQuestion();
		},
		
		createQuestion: function () {
			// Make ajax request
			
			// Add id to questionIds
			
			// move index to that
			
			// load question
		}
	}
	
	let quizId = 1;
	// Request for quiz from database
	$.ajax({
        url: `/api/quizzes/${quizId}`,
        method: "GET",
        dataType: "json",
        success: (quizDto) => {
			// Initialise the QuestionTracker
			QuestionTracker.init(quizDto);
			
			// -------------------- Event Listeners -----------------------
			// Previous Question
			$("#prev-question-btn").off("click").on("click", () => {
				QuestionTracker.prevQuestion();
			});
			
			// Save Question
			$("#save-question-btn").off("click").on("click", () => {
				QuestionTracker.saveQuestion();
			});
			
			// Next Question
			$("#next-question-btn").off("click").on("click", () => {
				QuestionTracker.nextQuestion();
			});
			
			// Create Question
			$("#create-question-btn").off("click").on("click", () => {
				QuestionTracker.createQuestion();
			});
		},
        error: (response) => {
			// If quiz can't be found, redirect to dashboard
			Router.navigate("dashboard");
        }
    });
	
});