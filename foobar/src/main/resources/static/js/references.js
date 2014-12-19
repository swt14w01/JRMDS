var projectName = $("#addRefInput");




projectName.keyup(function() {
	
    	var input = $("#addRefInput").val();
    	var project_title = $('.project_title').html();
    	var rule_title = $('.rule_title').html();
        $.ajax({
            url : '/testReferences',
            data: {'projectName' : project_title , 'ruleName' : rule_title, 'input' : input},
            type: 'GET',
            //dataType: "json",
            success : function(data) {
            	
            	//var test = $.parseJSON(data);
            	var itemHTML = "The following components are available in this project:<br/>";
            	if(jQuery.isEmptyObject(data)) {
            		 $('#result').hide('fast');
            		//$('#result').html('');
            	}
            	else {
            		 $('#result').show('fast');
            		 
            		 
            		 $.map(data, function(index, element) {
            		         itemHTML += ["<span class='item_count' style='margin-right:8px;'>",
            		                                        index,
            		                                        
            		                                        "<strong>",
            		                                        element,
            		                                        "</strong>",
            		                                        
            		                            "</span>"].join('\n');
            		       
            		    });
            		 $("#result").html(itemHTML);
            		}
            		 
            		// $('#result').html('<div class="item_count">' + data + '</div>');
            	
            					}
	
        			});

});
