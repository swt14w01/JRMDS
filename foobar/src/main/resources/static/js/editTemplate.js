var feld = 3;

function plus() {
	if (feld <= 7) {
		document.getElementById("dynamisch").innerHTML += "<Table border=0><TR><td>"
				+ feld
				+ "</td><td width='45'><input type='checkbox' ></td><td><input type='text' size='40'></td><td><input type='text' size='40'></td></tr></table>";
		feld++;
	}
}

function minus() {
	if (feld > 3) {
		feld--;
		document.getElementById("dynamisch").innerHTML = "";
		for (var zaehler = 3; zaehler < feld; zaehler++) {
			document.getElementById("dynamisch").innerHTML += "<Table border=0><TR><td>"
					+ zaehler
					+ "</td><td width='45'><input type='checkbox' ></td><td><input type='text' size='40'></td><td><input type='text' size='40'></td></tr></table>";
		}
	}
}

function confirmDelete() {
	window.location = "./confirmTemplateDelete.html"
}

function redirectToIndex() {
	window.location = "./index.html";

}
