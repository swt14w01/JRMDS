
var projectName = $("#projectName");
var submitButt = $("#createNewProjectButton");
var error = $(".error_message");


submitButt.attr("disabled", !((projectName.val()!="")));
error.hide();


projectName.keyup(function() {
	var isEmpty = !((projectName.val()!="")&&(projectName.val().length>3));
	submitButt.attr("disabled", isEmpty);
	
	if(isEmpty == true) {
		error.show("fast");
		
	}
	else {
		error.hide("fast");
	}

});