function toggle(targetId, toggle) {
	var element = document.getElementById(targetId)
	element.style.display = (element.style.display != 'none' ? 'none' : '');
	toggle.innerHTML = (element.style.display == 'none' ? '[+]' : '[-]');
}