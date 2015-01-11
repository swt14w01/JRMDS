var componentName = $("#componentName");
var submitButt = $("#updateComponentButton");
var error = $("#componentNameError");
var uneditedName = $('#componentName').val();


submitButt.attr("disabled", !((componentName.val()!="")));
error.hide();


componentName.keyup(function() {
	
	var projectName = $('#project_title').html();
	var componentType =  $('#componentType').val();
	var desiredComponentName = $('#componentName').val();
	var type = "";
	switch(componentType) {
		case "GROUP":
			type = "Group";
				break;
		case "CONCEPT":
			type = "Concept";
				break;
		case "CONSTRAINT":
			type = "Constraint";
				break;
		case "TEMPLATE":
			type = "Template";
				break;
	}

	if(desiredComponentName != uneditedName) {
	
    $.ajax({
        url : '/isGroupNameAvailable',
        data: {'projectName' : projectName, 'cName' : desiredComponentName, 'cType' : componentType},
        type: 'GET',
        success : function(data) {
        	
        	var isEmpty = !((componentName.val().length>=3));
        	
        	if(isEmpty == true || data == false) {
        		error.html("Your " + type + " name must not be empty or shorter than 3 characters.");
        		error.show("fast");
        		if(data == false) {
        			error.html("This " + type +  " name is already taken. Please choose another one.");
        		}
        		
        	}
        	else {
        		error.hide("fast");
        	}
        	
        	
        	submitButt.attr("disabled", (!(data) || (isEmpty)));
        	
        }
    });

    
	}


});