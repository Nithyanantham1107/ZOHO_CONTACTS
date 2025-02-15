/**
 * 
 */


/**
 * 
 */



const createGroupButton = document.getElementById("groupbutton");
const checkboxes = document.querySelectorAll('.contact-checkbox');
const createsidebars = document.getElementById('sidebar');
const viewsidebars = document.getElementById('viewsidebar');


createGroupButton.onclick = function() {


	createsidebars.style.display = "flex";
	checkboxes.forEach(checkbox => {
		checkbox.checked = false;
	});




}





var closecreatemodel = document.getElementById("closecreatemodel");
closecreatemodel.onclick = function() {

	createsidebars.style.display = "none";


}







function groupView(e) {
	e.preventDefault();
	const form = e.target;
	const formelements = form.elements;


	var groupID = formelements["groupid"].value;
	var method = formelements["method"].value;

	const formData = new URLSearchParams();
	formData.append("groupid", groupID);
	formData.append("method", method);

	viewsidebars.style.display = "flex";

	fetch('http://localhost:8080/grouplist', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: formData.toString(),
	})
		.catch(error => {

			console.log(error)
		})








}
document.getElementById('mergeContact').onclick = function() {
	
	

	const selectedIds = Array.from(checkboxes)
		.filter(checkbox => checkbox.checked)
		.map(checkbox => checkbox.value);

	if (selectedIds.length === 0) {
		alert("Please select at least one contact.");
		return;
	}


	const form = document.createElement('form');
	form.method = 'post';
	form.action = '/mergecontact';
	




	selectedIds.forEach(id => {
		const idInput = document.createElement('input');
		idInput.type = 'hidden';
		idInput.name = 'contact_ids';
		idInput.value = id;
		form.appendChild(idInput);
	});

	document.body.appendChild(form);
	form.submit();
}
