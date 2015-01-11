var count =$("#paramDiv span").size();
var newParamButton = $("#newParamButton");
var paramDiv = $("#paramDiv");

alert(count);

newParamButton.click(function() {
	

	var newParam = "<span><input type='text' class='parameter_input' name='toUpdateName'/> <input type='text' class='parameter_input' name='toUpdateValue'/> <input type='checkbox' name='isString'/> <input type='hidden' name='toUpdateId' value='" + count + "'/></span>";
	paramDiv.append(newParam);
	count = $("#paramDiv span").size();
	alert(count);

	
});
