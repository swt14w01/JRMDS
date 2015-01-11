var projectName = $("#addRefInput");

var delay = (function(){
	  var timer = 0;
	  return function(callback, ms){
	    clearTimeout (timer);
	    timer = setTimeout(callback, ms);
	  };
	})();


projectName.keyup(function() {
		
	
	delay(function(){
	
    	var input = $("#addRefInput").val();
    	var project_title = $('.project_title').html();
    	var rule_title = $('.rule_title').html();
    	var rule_type = $('#rType').val();
    	
    	var new_severity = $('#newSeverity').val();
  
    	
        $.ajax({
            url : '/testReferences',
            data: {'projectName' : project_title , 'ruleName' : rule_title, 'input' : input, 'ruleType' : rule_type},
            type: 'GET',
            success : function(data) {
            	
            	var itemHTML = "The following components suitable for a reference were found:<br/>";
            	

            	
            	if(jQuery.isEmptyObject(data)) {
            		 $('#result').hide('fast');
            	}
            	else {
            		 $('#result').show('fast');
            		 
            		 
            		 $.map(data, function(index, element) {
            			 var img, serviceURL = "";
            			 
                     	if(rule_type != "GROUP") {
                    		serviceURL = "<a href='/referenceRule?project=" + $('.project_title').html() + "&rRefID=" + rule_title + "&rType=" + rule_type + "&newRefID=" + element + "&newType=" + index + "'>";
                    	}
                     	else {
                     		serviceURL = "<a href='/referenceGroup?project=" + $('.project_title').html() + "&gRefID=" + rule_title + "&newRefID=" + element + "&newType=" + index + "&newSeverity=" + new_severity + "'>";
                     	}
            			 
            			 	if(index == "CONCEPT") {
            			 		img = 'concept';
            			 	}
            			 	if(index == "CONSTRAINT") {
            			 		img = 'constraint';
            			 	}
            			 	if(index == "TEMPLATE") {
            			 		img = 'template';
            			 	}
            			 	
            			 	if(index == "GROUP") {
            			 		img = 'group';
            			 	}
            			 	
            		         itemHTML += ["<span class='item_count' style='margin-right:8px;'>",
            		                                        "<img src='../img/"+img+".png' class='symbol_small' />",
            		                                        
            		                                        serviceURL,
            		                                        element,
            		                                        "</a>",
            		                                        
            		                            "</span>"].join('\n');
            		         
            		    
            		       
            		    });
            		 
            		 $("#result").html(itemHTML);
            		}
            		 
            		// $('#result').html('<div class="item_count">' + data + '</div>');
            	
            					}
	
        			});
        
	 }, 250 );

});
