document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');

    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            const inputs = form.querySelectorAll('.form-input');
            let valid = true;
            let message = '';

            inputs.forEach(function(input) {
                if (input.type === 'hidden') return;

                if (input.value.trim() === '') {
                    valid = false;
                    input.style.borderColor = '#dc2626';
                    message = 'Veuillez remplir tous les champs.';
                    return;
                }

                input.style.borderColor = '#D8D8D8';

                if (input.type === 'email') {
                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(input.value)) {
                        valid = false;
                        input.style.borderColor = '#dc2626';
                        message = 'Veuillez entrer un email valide.';
                    }
                }

                if (input.type === 'password') {
                    var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
                    if (!passwordRegex.test(input.value)) {
                        valid = false;
                        input.style.borderColor = '#dc2626';
                        message = 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
                    }
                }
            });

            if (!valid) {
                event.preventDefault();
                alert(message);
            }
        });
    });
});