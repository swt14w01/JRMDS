
var projectName = $("#projectName");
var submitButt = $("#createNewProjectButton");
var error = $(".error_message");


submitButt.attr("disabled", !((projectName.val()!="")));
error.hide();


projectName.keyup(function() {
	
	
	
    	var desiredProjectName = $('#projectName').val();
    	var json = {'pName' : desiredProjectName};

        $.ajax({
            url : '/isProjectNameAvailable',
            data: {'pName' : $('#projectName').val()},
            type: 'GET',
            success : function(data) {
               // $('#result').html(data);
            	
            	var isEmpty = !((projectName.val()!="")&&(projectName.val().length>=3));
            	//submitButt.attr("disabled", isEmpty);
            	
            	if(isEmpty == true || data == false) {
            		error.show("fast");
            		if(data == false) {
            			error.html("This Project name is already taken. Please choose another one.");
            		}
            		
            	}
            	else {
            		error.hide("fast");
            	}
            	

            	
            	
            	submitButt.attr("disabled", (!(data) || (isEmpty)));
            	
            }
        });
	
	

});