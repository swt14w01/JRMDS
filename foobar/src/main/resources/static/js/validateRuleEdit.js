var ruleName = $("#ruleName");
var submitButt = $("#createNewRuleButton");
var error = $("#ruleNameError");
var uneditedName = $('#ruleName').val();

var rType = $("$rType").val();



ruleName.keyup(function() {
	
	
	var desiredRuleName = $('#ruleName').val();

	if(desiredRuleName != uneditedName) {
	
    $.ajax({
        url : '/isGroupNameAvailable',
        data: {'projectName' : $('#project_title').html(),'gName' : $('#groupName').val()},
        type: 'GET',
        success : function(data) {
        	
        	var isEmpty = !((ruleName.val().length>=3));
        	
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