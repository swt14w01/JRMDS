var groupName = $("#groupName");
var submitButt = $("#createNewGroupButton");
var error = $("#groupNameError");
var uneditedName = $('#groupName').val();


submitButt.attr("disabled", !((groupName.val()!="")));
error.hide();


groupName.keyup(function() {
	
	
	var desiredGroupName = $('#groupName').val();

	if(desiredGroupName != uneditedName) {
	
    $.ajax({
        url : '/isGroupNameAvailable',
        data: {'projectName' : $('#project_title').html(),'gName' : $('#groupName').val()},
        type: 'GET',
        success : function(data) {
        	
        	var isEmpty = !((groupName.val().length>=3));
        	
        	if(isEmpty == true || data == false) {
        		error.html("Your Group name must not be empty or shorter than 3 characters.");
        		error.show("fast");
        		if(data == false) {
        			error.html("This Group name is already taken. Please choose another one.");
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