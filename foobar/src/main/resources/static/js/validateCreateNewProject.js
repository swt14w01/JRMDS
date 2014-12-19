
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
            	
            	var isEmpty = !((projectName.val()!="")&&(projectName.val().length>=3));
            	
            	if(isEmpty == true) {
            		error.show("fast");
            		
            	}
            	else {
            		error.hide("fast");
            	}
            	
            	
            	submitButt.attr("disabled", (!(data) || (isEmpty)));
            	
            }
        });
	
	

});