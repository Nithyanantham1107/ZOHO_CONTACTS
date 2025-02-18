document.getElementById('addphone').addEventListener('click', function() {
	const inputContainer = document.createElement('div');
	inputContainer.classList.add('input-container');

	const inputField = document.createElement('input');
	inputField.type = 'text';
	inputField.name = 'phones';
	inputField.placeholder = 'Enter PhoneNo';


	const inputField2 = document.createElement('input');

	inputField2.type = 'hidden';
	inputField2.name = 'phoneID';
	inputField2.value = "-1"
	inputField2.placeholder = 'Enter value';

	const divContainer = document.createElement('div');
	divContainer.classList.add('datalabel');

	const inputField3 = document.createElement('input');

	inputField3.type = 'text';
	inputField3.name = 'phonelabels';
	inputField3.placeholder = 'label';

	divContainer.appendChild(inputField3);


	const removeBtn = document.createElement('span');
	removeBtn.textContent = 'X';
	removeBtn.classList.add('remove-btn');
	removeBtn.addEventListener('click', function() {
		inputContainer.remove();
	});

	inputContainer.appendChild(inputField);
	inputContainer.appendChild(divContainer);
	inputContainer.appendChild(removeBtn);
	inputContainer.appendChild(inputField2)
	document.getElementById('Phonelist').appendChild(inputContainer);
});


document.getElementById('addEmail').addEventListener('click', function() {
	const inputContainer = document.createElement('div');
	inputContainer.classList.add('input-container');

	const inputField = document.createElement('input');
	inputField.type = 'text';
	inputField.name = 'emails';
	inputField.placeholder = 'Enter Email';



	const inputField2 = document.createElement('input');

	inputField2.type = 'hidden';
	inputField2.name = 'emailID';
	inputField2.value = "-1"
	inputField2.placeholder = 'Enter value';
	const divContainer = document.createElement('div');
	divContainer.classList.add('datalabel');

	const inputField3 = document.createElement('input');

	inputField3.type = 'text';
	inputField3.name = 'emaillabels';
	inputField3.placeholder = 'label';


	divContainer.appendChild(inputField3);


	const removeBtn = document.createElement('span');
	removeBtn.textContent = 'X';
	removeBtn.classList.add('remove-btn');
	removeBtn.addEventListener('click', function() {
		inputContainer.remove();
	});

	inputContainer.appendChild(inputField);
	inputContainer.appendChild(divContainer);
	inputContainer.appendChild(removeBtn);
	inputContainer.appendChild(inputField2);
	document.getElementById('emaillist').appendChild(inputContainer);
});


function removeField(button) {

	button.parentElement.remove();
}