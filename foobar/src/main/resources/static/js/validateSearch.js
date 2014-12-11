alert("test");
var submitButt = $("#search_button");
var searchInput = $("#search_input");



submitButt.attr("disabled", !((searchInput.val()!="Search JRMDS")) && (searchInput.val().length>0)));

searchInput.blur(function() {

	submitButt.attr("disabled", !((searchInput.val()!="Search JRMDS")) && (searchInput.val().length>0)));

});