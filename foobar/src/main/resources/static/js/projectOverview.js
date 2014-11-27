jQuery(document).ready(function() {
	$("#menu_group").click(function() {
		$("#tab1").toggle("fast");
		$(this).toggleClass("active");
	});
	$("#menu_concept").click(function() {
		$("#tab2").toggle("fast");
		$(this).toggleClass("active");
	});
	$("#menu_contraint").click(function() {
		$("#tab3").toggle("fast");
		$(this).toggleClass("active");
	});
	$("#menu_template").click(function() {
		$("#tab4").toggle("fast");
		$(this).toggleClass("active");
	});
});