var inputfield  = $("#tagHolder");
var delimiter = ';';
var input = inputfield.val();
var splittedTags = input.split(delimiter);
var length = splittedTags.length;

for(var i=0; i< splittedTags.length-1; i++) {
	
$('#tagRep').append('<div class="tag">' + splittedTags[i] + '</div>');


}

inputfield.keyup(function() {
	
	var lastCharacter = inputfield.val().substr(inputfield.val().length - 1);

	
	
	

	if(lastCharacter == delimiter) {
		//alert(inputfield.val());
		input = inputfield.val();
		splittedTags = input.split(delimiter);
	
		var lastTag = splittedTags[splittedTags.length - 2];
		$('#tagRep').append('<div class="tag">' + lastTag + '</div>');
		
		
		//alert(last);
	}
	
	
	
	//alert(lastCharacter);
	
});