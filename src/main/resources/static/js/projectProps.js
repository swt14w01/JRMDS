function Hinzufuegen() {
	var Elementknoten = document.createElement(URI);
	var Textknoten = document
			.createTextNode(document.Formular.Elementinhalt.value);
	Elementknoten.appendChild(Textknoten);
	document.getElementById("User").appendChild(Elementknoten);
}