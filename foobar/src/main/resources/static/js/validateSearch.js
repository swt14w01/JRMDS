
alert("sd");
var submitButt = $(".searchButton");
var searchInput = $("#search_input");
var searchJRMDS ="Search JRMDS";
// INIT BUTTON
submitButt.attr("disabled", !(searchInput.val()!=searchJRMDS && searchInput.val()!=""));


// WHILE TYPING, CHANGE BUTTON
searchInput.keyup(function() {
	submitButt.attr("disabled", !(searchInput.val()!=searchJRMDS && searchInput.val()!=""));

});

// WHEN CLICKING ON SEARCHFIELD, SET VALUE TO ""
searchInput.focus(function() {
	if (searchInput.val() == searchJRMDS) {
		searchInput.val("");
	}

});

// WHEN CLICKING OUTSIDE SEARCHFIELD, RESET VALUE TO "Search JRMDS"
searchInput.blur(function() {
	if (searchInput.val().length == 0) {
		searchInput.val(searchJRMDS);
	}
});