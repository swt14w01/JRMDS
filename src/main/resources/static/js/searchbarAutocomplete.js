

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
