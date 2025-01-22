/**
 * 
 */


const viewGroupButton = document.getElementById("closeviewmodel");

const createGroupButton = document.getElementById("groupbutton");
const checkboxes = document.querySelectorAll('.contact-checkbox');
const createsidebars = document.getElementById('sidebar');
const viewsidebars = document.getElementById('viewsidebar');
createGroupButton.onclick = function() {


	createsidebars.style.display = "block";
	checkboxes.forEach(checkbox => {
		checkbox.checked = false;
	});




}





document.getElementById('submitGroup').onclick = function() {
	const groupName = document.getElementById('groupNamecreate').value.trim();
	const method = document.getElementById('methodcreate').value.trim();
	const groupid = document.getElementById('groupidcreate').value.trim();

	if (!groupName) {
		alert("Please enter a group name.");
		return;
	}

	const selectedIds = Array.from(checkboxes)
		.filter(checkbox => checkbox.checked)
		.map(checkbox => checkbox.value);

	if (selectedIds.length === 0) {
		alert("Please select at least one contact.");
		return;
	}


	const form = document.createElement('form');
	form.method = 'post';
	form.action = '/creategroup';


	const groupNameInput = document.createElement('input');
	groupNameInput.type = 'hidden';
	groupNameInput.name = 'groupName';
	groupNameInput.value = groupName;
	form.appendChild(groupNameInput);


	const methoddata = document.createElement('input');
	methoddata.type = 'hidden';
	methoddata.name = 'methodtype';
	methoddata.value = method;
	form.appendChild(methoddata);


	const group = document.createElement('input');
	group.type = 'hidden';
	group.name = 'groupdata';
	group.value = groupid;
	form.appendChild(group);






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


var closecreatemodel = document.getElementById("closecreatemodel");
closecreatemodel.onclick = function() {

	createsidebars.style.display = "none";


}





viewGroupButton.onclick = function() {


	viewsidebars.style.display = "none";





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

	viewsidebars.style.display = "block";

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