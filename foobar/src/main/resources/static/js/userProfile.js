$(document).ready(function() {
	$("#username_edit_button").click(function(submit_event) {
		
		submit_event.preventDefault();
		
		var toggleWidthEditAnimationField = ($("#user_edit_animation_field").width() == 310) ? "0px" : "310px";
		var toggleWidthSingleForm         = ($("#user_single_form").width() == 200) ? "500px" : "200px";
        var toggleOpacityEditAnimationField
        var open
        switch(document.getElementById("user_edit_animation_field").style.opacity) {
                            case "" : toggleOpacityEditAnimationField = 1; open = false; break;
                            case "1": toggleOpacityEditAnimationField = 0; open = true;  break;
                            case "0": toggleOpacityEditAnimationField = 1; open = false; break;
        }

        $("#user_edit_animation_field")
        .animate({
        "width":   toggleWidthEditAnimationField,
        "opacity": toggleOpacityEditAnimationField
        });
		
		$("#user_single_form")
        .animate({
        "width": toggleWidthSingleForm
        });
		
		if(open) {
			if(document.username_form.newUsername.value != "") {
			//document.form_name.feld_name.value
				setTimeout(function() {				
				$("#username_form").submit();   
				}, 500);
			}
		}
	
	});
});

$(document).ready(function() {
	$("#email_adress_edit_button").click(function(submit_event) {
		
		submit_event.preventDefault();
		
		var toggleWidthEditAnimationField = ($("#email_adress_edit_animation_field").width() == 310) ? "0px" : "310px";
		var toggleWidthSingleForm         = ($("#email_adress_single_form").width() == 200) ? "500px" : "200px";
        var toggleOpacityEditAnimationField
        var open
        switch(document.getElementById("email_adress_edit_animation_field").style.opacity) {
                            case "" : toggleOpacityEditAnimationField = 1; open = false; break;
                            case "1": toggleOpacityEditAnimationField = 0; open = true;  break;
                            case "0": toggleOpacityEditAnimationField = 1; open = false; break;
        }

        $("#email_adress_edit_animation_field")
        .animate({
        "width":   toggleWidthEditAnimationField,
        "opacity": toggleOpacityEditAnimationField
        });
		
		$("#email_adress_single_form")
        .animate({
        "width": toggleWidthSingleForm
        });
		
		if(open) {
			if(document.email_adress_form.newEmailAdress.value != "") {
			//document.form_name.feld_name.value
				setTimeout(function() {				
				$("#email_adress_form").submit();   
				}, 500);
			}
		}
	
	});
});