jQuery(function($) {
	var popUpState = false;

	$("#addRule").click(function() {
		if (popUpState == false) {
			$("#add_rule_menu").fadeIn("fast");
			$(this).css('background-image','none');
			$(this).css('color','#0086e4');
			popUpState = true;
		}
		return false;
	});

	$("body").click(function() {
		if (popUpState == true) {
			$("#add_rule_menu").fadeOut("fast");
			$("#addRule").css('background-image','url(../img/options_button_bg.png)');
			$("#addRule").css('color','black');
			popUpState = false;
		}

	});

});