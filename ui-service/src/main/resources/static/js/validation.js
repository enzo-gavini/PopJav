document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');

    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            const inputs = form.querySelectorAll('.form-input');
            let valid = true;

            inputs.forEach(function(input) {
                if (input.type === 'hidden') return;

                if (input.value.trim() === '') {
                    valid = false;
                    input.style.borderColor = '#dc2626';
                } else {
                    input.style.borderColor = '#D8D8D8';
                }
            });

            if (!valid) {
                event.preventDefault();
                alert('Veuillez remplir tous les champs.');
            }
        });
    });
});