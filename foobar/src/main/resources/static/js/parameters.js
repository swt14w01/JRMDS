var count =$("#paramDiv span").size();
var newParamButton = $("#newParamButton");
var paramDiv = $("#paramDiv");


$('.removeButton').click(function() {
	
	$(this).parents('span').fadeOut(300, function() {$(this).remove();});
	//$(this).parents('span').fadeOut();
});


$('.cancelButton').click(function() {
	
	$(this).parents('span').fadeOut(300, function() {$(this).remove();});
	//$(this).parents('span').fadeOut();
});


newParamButton.click(function() {
	


	var newParam = "<span>" +

						"<input type='button' class='removeButton' value='-' />"+
						"<input type='text' class='parameter_input' name='toUpdateName'/> " +
						"<input type='text' class='parameter_input' name='toUpdateValue'/> " +
						"<input type='checkbox' name='isString' value='" + count + "'/> " +
						"<input type='hidden' name='toUpdateId' value='" + count + "'/>" +
						
					"</span>";
	paramDiv.append(newParam);
	count = $("#paramDiv span").size();


	$('.removeButton').click(function() {

		
		$(this).parents('span').fadeOut(300, function() {$(this).remove();});
		// $(this).parents('span').remove();

		 
	});

	
});
