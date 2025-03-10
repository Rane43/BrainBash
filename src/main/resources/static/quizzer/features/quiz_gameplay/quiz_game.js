const QuizGame = {
	quizId: null,
    currentQuestionIndex: 0,
    score: 0,
    questionIds: [],
    currentQuestion: null,
    selectedAnswerId: null,  // Tracks the selected answer id

    init(quizGameDto) {
		this.quizId = quizGameDto.id;
        this.questionIds = quizGameDto.questionIds;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.loadQuestion();
    },

    loadQuestion() {
        if (this.currentQuestionIndex >= this.questionIds.length) {
            this.endGame();
            return;
        }

        const questionId = this.questionIds[this.currentQuestionIndex];
        this.selectedAnswerId = null;  // Reset selected answer
        this.fetchQuestion(questionId);

        $('#next-btn').hide();
    },

    fetchQuestion(questionId) {
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

    submitAnswer() {
        if (!this.currentQuestion || !this.selectedAnswerId) return;
		
		const correctAnswerId = String(this.currentQuestion.answerDtos
		    .find(a => a.correct)?.id);


        // Check if the user's selected answer is correct
        const isCorrect = correctAnswerId === String(this.selectedAnswerId);
        if (isCorrect) {
            this.score++;
        }

        // Provide feedback to the user
        this.showAnswerFeedback(correctAnswerId);

        // If the question isn't the last, display next, otherwise, finish button
        if (this.currentQuestionIndex < this.questionIds.length - 1) {
            $('#next-btn').show();
        } else {
            $('#finish-btn').show();
        }
    },

    showAnswerFeedback(correctAnswerId) {
		let answerBtn = $(`#${this.selectedAnswerId}`);
		if (correctAnswerId === this.selectedAnswerId) {
			answerBtn.addClass("correct-answer");	
		} else {
			answerBtn.addClass("wrong-answer");
			$(`#${correctAnswerId}`).addClass("correct-answer");
		}
		
		// Hide next/finish buttons after feedback
	    $('#next-btn').hide();
	    $('#finish-btn').hide();

	    // Show appropriate button
	    if (this.currentQuestionIndex < this.questionIds.length - 1) {
	        $('#next-btn').show();
	    } else {
	        $('#finish-btn').show();
	    }
    },

    endGame() {
		// Save to database and then display end game view
		if (!TokenStorage.isLoggedIn()) {
			TokenStorage.logout();
			return;
		}
		
		$.ajax({
		    url: `/api/points?quiz_id=${this.quizId}`,
		    method: "PUT",
		    headers: {
		        "Authorization": `Bearer ${TokenStorage.getToken()}`
		    },
		    contentType: "application/json",
		    data: JSON.stringify({
		        quizId: this.quizId,
		        points: this.score
		    }),
		    dataType: "text",
		    success: (highestPoints) => {
		        $("#gameplay-menu").hide();
		        $("#finish-game-menu").show();
		        $("#FinalResult").text(`${this.score}!`);
				$("#highest-score").text(highestPoints);
		    },
		    error: () => {
		        // Handle error if needed
		    }
		});
    },

    // Additional methods for question navigation
    goToNextQuestion() {
        if (this.currentQuestionIndex < this.questionIds.length - 1) {
            this.currentQuestionIndex++;
            this.loadQuestion();
        }
    },
};




// ----------------- Document ready setup ------------------
$(document).ready(function () {
    // Uncomment and adjust as needed for initializing the quiz
    $.ajax({
        url: "/api/quizzes/1",
        method: "GET",
        dataType: "json",
        success: (quizDto) => QuizGame.init(quizDto),
        error: (response) => {
            console.log(`Issue retrieving quiz... ${response.responseText}`);
        }
    });

    $("#play-btn").off("click").on("click", () => {
		$("#game-main-menu").hide();
		$("#gameplay-menu").show();
        QuizGame.loadQuestion();
    });

    // Navigation buttons for next and submit buttons
    $("#next-btn").on("click", () => QuizGame.goToNextQuestion());
	
	$("#finish-btn").on("click", () => {
		QuizGame.endGame();
	});
});






// ----------------- TEMPLATES -------------------
const QuizTemplates = {
    answerButton: function (answer) {
        return $("<button>")
            .attr("type", "button")
            .addClass("question-btn question-answer-btn")
            .text(answer.text);
    }
};
