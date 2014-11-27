var nameElement = document.forms.search.search_input;
var placeHolder = "Suchen auf JRMDS";

function nameFocus(e) {
	var element = e.target || window.event.srcElement;
	if (element.value == placeHolder) {

		element.value = "";
		element.style.color = 'black';
	}

}

function nameBlur(e) {
	var element = e.target || window.event.srcElement;
	if (element.value == "") {
		element.value = placeHolder;
		element.style.color = '#d6d6d6';
	}
}

if (nameElement.addEventListener) {
	nameElement.addEventListener("focus", nameFocus, false);
	nameElement.addEventListener("blur", nameBlur, false);
} else if (nameElement.attachEvent) {
	nameElement.attachEvent("onfocus", nameFocus);
	nameElement.attachEvent("onblur", nameBlur);
}



$('#search_input').autocomplete({
	serviceUrl : '/getAutoCompleteSuggestions',
	paramName : "tagName",
	delimiter : ",",
	transformResult : function(response) {

		return {
			//must convert json to javascript object before process
			suggestions : $.map($.parseJSON(response), function(item) {

				return {
					value : item.refID,
					data : item.id
				};
			})

		};

	}

});
