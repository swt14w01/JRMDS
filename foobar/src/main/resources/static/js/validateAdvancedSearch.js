var checkboxes = $("input[type='checkbox']");
var submitButt = $("#advancedSearch");
var searchInput = $("#search_input");

submitButt.attr("disabled", !((searchInput.val()!="Search JRMDS") && (checkboxes.is(":checked")) && (searchInput.val().length>0)));

checkboxes.click(function() {
	submitButt.attr("disabled", !((searchInput.val()!="Search JRMDS") && (checkboxes.is(":checked")) && (searchInput.val().length>0)));

});
searchInput.blur(function() {

	submitButt.attr("disabled", !((searchInput.val()!="Search JRMDS") && (checkboxes.is(":checked")) && (searchInput.val().length>0)));

});