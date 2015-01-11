var count =$("#paramDiv span").size();
var newParamButton = $("#newParamButton");
var paramDiv = $("#paramDiv");


newParamButton.click(function() {
	

	var newParam = "<span><input type='text' class='parameter_input' th:name='toUpdateName'/> <input type='text' class='parameter_input' th:name='toUpdateValue'/> <input type='checkbox' th:name='isString'/> <input type='hidden' th:name='toUpdateId' th:value='" + count + "'/></span>";
	paramDiv.append(newParam);
	count = $("#paramDiv span").size();

	
});
