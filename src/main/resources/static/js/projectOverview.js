jQuery(document).ready(function() {
	
	$("#tab1").show();
	if($("#nGroups").html()=="0") {
		$("#menu_group").toggleClass("inactive");
	}
	else {
		$("#menu_group").toggleClass("active");
		
		$("#menu_group").click(function() {
			$("#tab1").toggle("fast");
			$(this).toggleClass("active");
		});
		
	}
	
	$("#tab2").show();
	if($("#nConcepts").html()=="0") {
		$("#menu_concept").toggleClass("inactive");
	}
	else {
		$("#menu_concept").toggleClass("active");
		$("#menu_concept").click(function() {
			$("#tab2").toggle("fast");
			$(this).toggleClass("active");
		});
	}
	
	
	$("#tab3").show();
	if($("#nConstraints").html()=="0") {
		$("#menu_contraint").toggleClass("inactive");
	}
	else {
		$("#menu_contraint").toggleClass("active");
		$("#menu_contraint").click(function() {
			$("#tab3").toggle("fast");
			$(this).toggleClass("active");
		});
	}
	
	$("#tab4").show();
	if($("#nTemplates").html()=="0") {
		$("#menu_template").toggleClass("inactive");
	}
	else {
		$("#menu_template").toggleClass("active");
		$("#menu_template").click(function() {
			$("#tab4").toggle("fast");
			$(this).toggleClass("active");
		});
	}
		

});