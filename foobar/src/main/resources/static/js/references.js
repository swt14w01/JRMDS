var projectName = $("#addRefInput");




projectName.keyup(function() {
	
    	var desiredProjectName = $("#addRefInput").val();
    	var project_title = $('.project_title').html();
    	var rule_title = $('.rule_title').html();
        $.ajax({
            url : '/testReferences',
            data: {'projectName' : project_title , 'ruleName' : rule_title},
            type: 'GET',
            success : function(data) {
                $('#result').html('<div class="item_count">' + data + '</div>');

            	
            }
        });
	
	

});