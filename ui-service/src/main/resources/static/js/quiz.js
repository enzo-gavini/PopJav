document.addEventListener('DOMContentLoaded', function() {
    const questions = document.querySelectorAll('.quiz-question');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const submitBtn = document.getElementById('submit-btn');
    const counter = document.getElementById('question-counter');
    const timerDisplay = document.getElementById('quiz-timer');
    let current = 0;

    function showQuestion(index) {
        questions.forEach(function(q, i) {
            q.style.display = i === index ? 'block' : 'none';
        });
        counter.textContent = (index + 1) + ' / ' + questions.length;
        prevBtn.style.display = index === 0 ? 'none' : 'inline-block';
        if (index === questions.length - 1) {
            nextBtn.style.display = 'none';
            submitBtn.style.display = 'inline-block';
        } else {
            nextBtn.style.display = 'inline-block';
            submitBtn.style.display = 'none';
        }
    }

    prevBtn.addEventListener('click', function() {
        if (current > 0) { current--; showQuestion(current); }
    });

    nextBtn.addEventListener('click', function() {
        if (current < questions.length - 1) { current++; showQuestion(current); }
    });

    showQuestion(0);

    if (timerDisplay) {
        var totalSeconds = questions.length * 30;

        var interval = setInterval(function() {
            var minutes = Math.floor(totalSeconds / 60);
            var seconds = totalSeconds % 60;
            timerDisplay.textContent = minutes + ':' + (seconds < 10 ? '0' : '') + seconds;

            if (totalSeconds <= 30) {
                timerDisplay.style.color = '#dc2626';
            }

            if (totalSeconds <= 0) {
                clearInterval(interval);
                document.querySelector('form').submit();
            }

            totalSeconds--;
        }, 1000);
    }
});