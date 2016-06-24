$( document ).ready(function() {
	var i = 0;
	$("#addCharacter").on("click", function(){
		var clicked = $("#characters").val();
		$("#charactersOption").append("<option path='characters["+i+"]' value='"+clicked+"'>"+clicked+"</option>").val();
		i++;
	});
	
});