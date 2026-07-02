document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    var isRegisterPage = window.location.pathname.includes('/register');

    forms.forEach(function(form) {
        // Zone d'erreur accessible : role="alert" est annoncé par les lecteurs d'écran.
        var errorBox = form.querySelector('.form-error');
        if (!errorBox) {
            errorBox = document.createElement('p');
            errorBox.className = 'form-error';
            errorBox.setAttribute('role', 'alert');
            errorBox.hidden = true;
            form.insertBefore(errorBox, form.firstChild);
        }

        form.addEventListener('submit', function(event) {
            const inputs = form.querySelectorAll('.form-input');
            let valid = true;
            let message = '';

            inputs.forEach(function(input) {
                if (input.type === 'hidden') return;

                input.style.borderColor = '#D8D8D8';
                input.setAttribute('aria-invalid', 'false');
                var fieldInvalid = false;

                if (input.value.trim() === '') {
                    fieldInvalid = true;
                    message = 'Veuillez remplir tous les champs.';
                } else if (input.type === 'email') {
                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(input.value)) {
                        fieldInvalid = true;
                        message = 'Veuillez entrer un email valide.';
                    }
                } else if (input.type === 'password' && isRegisterPage) {
                    var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
                    if (!passwordRegex.test(input.value)) {
                        fieldInvalid = true;
                        message = 'Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.';
                    }
                }

                if (fieldInvalid) {
                    valid = false;
                    input.style.borderColor = '#dc2626';
                    input.setAttribute('aria-invalid', 'true');
                }
            });

            if (!valid) {
                event.preventDefault();
                errorBox.textContent = message;
                errorBox.hidden = false;
            } else {
                errorBox.hidden = true;
                errorBox.textContent = '';
            }
        });
    });
});
