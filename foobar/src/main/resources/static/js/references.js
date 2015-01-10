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
            		                                        
            		                                        "<a href='/referenceRule?project=" + $('.project_title').html() + "&rRefID=" + rule_title + "&rType=" + rule_type + "&newRefID=" + element + "&newType=" + index + "'>",
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
