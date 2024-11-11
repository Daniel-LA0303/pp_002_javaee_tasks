function getCookie(name) {
                
	const nameEQ = name + "=";
	const cookies = document.cookie.split(';');
	for (let cookie of cookies) {
   
		cookie = cookie.trim();

		if (cookie.startsWith(nameEQ)) {

			return cookie.substring(nameEQ.length);
		}
	}
	return null; 
}