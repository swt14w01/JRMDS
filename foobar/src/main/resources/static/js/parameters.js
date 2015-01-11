var count =$("#paramDiv span").size();
var newParamButton = $("#newParamButton");
var paramDiv = $("#paramDiv");


newParamButton.click(function() {
	
	var newParam = "<span><input type='text' class='parameter_input' name='parameter " + count + " '/> <input type='text' class='parameter_input' name='parameter_data" + count + "'/> <input type='checkbox' name='checkBox" + count + "'/></span>";
	paramDiv.append(newParam);
	count = $("#paramDiv span").size();
	alert(count);
	
});