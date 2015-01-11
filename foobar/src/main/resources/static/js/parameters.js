var count =$("#paramDiv span").size();
var newParamButton = $("#newParamButton");
var paramDiv = $("#paramDiv");


$('.cancelButton').click(function() {
	
	$(this).parents('span').fadeOut(300, function() {$(this).remove();});
	//$(this).parents('span').fadeOut();
});


newParamButton.click(function() {
	


	var newParam = "<span>" +
						"<input type='text' class='parameter_input' name='toUpdateName'/> " +
						"<input type='text' class='parameter_input' name='toUpdateValue'/> " +
						"<input type='checkbox' name='isString'/> " +
						"<input type='hidden' name='toUpdateId' value='" + count + "'/>" +
						"<input type='button' class='cancelButton' value='-' />"+
					"</span>";
	paramDiv.append(newParam);
	count = $("#paramDiv span").size();

	$('.cancelButton').click(function() {
		
		$(this).parents('span').fadeOut(300, function() {$(this).remove();});
		// $(this).parents('span').remove();

		 
	});

	
});

