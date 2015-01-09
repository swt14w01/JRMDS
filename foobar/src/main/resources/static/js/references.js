var projectName = $("#addRefInput");




projectName.keyup(function() {
	
    	var input = $("#addRefInput").val();
    	var project_title = $('.project_title').html();
    	var rule_title = $('.rule_title').html();
    	var rule_type = $('#rType').val();
        $.ajax({
            url : '/testReferences',
            data: {'projectName' : project_title , 'ruleName' : rule_title, 'input' : input, 'ruleType' : rule_type},
            type: 'GET',
            //dataType: "json",
            success : function(data) {
            	
            	//var test = $.parseJSON(data);
            	var itemHTML = "Are you looking for one of these Components?<br/>";
            	if(jQuery.isEmptyObject(data)) {
            		 $('#result').hide('fast');
            		//$('#result').html('');
            	}
            	else {
            		 $('#result').show('fast');
            		 
            		 
            		 $.map(data, function(index, element) {
            			 var img = "";
            			 	if(index == "CONCEPT") {
            			 		img = 'concept';
            			 	}
            			 	if(index == "CONSTRAINT") {
            			 		img = 'constraint';
            			 	}
            			 	if(index == "TEMPLATE") {
            			 		img = 'template';
            			 	}
            		         itemHTML += ["<span class='item_count' style='margin-right:8px;'>",
            		                                        "<img src='../img/"+img+".png' class='symbol_small' />",
            		                                        
            		                                        "<strong>",
            		                                        element,
            		                                        "</strong>",
            		                                        
            		                            "</span>"].join('\n');
            		         
            		         jQuery('<div/>', {
            		        	    href: 'http://google.com',
            		        	    title: 'Become a Googler',
            		        	    rel: 'external',
            		        	    text: 'Go to Google!'
            		        	}).appendTo('#result');
            		       
            		    });
            		 
            		 $("#result").html(itemHTML);
            		}
            		 
            		// $('#result').html('<div class="item_count">' + data + '</div>');
            	
            					}
	
        			});

});
