


function toast(message) {
  
    if(performance.navigation.type == 2) return;

    let snackbar = document.createElement("div");
    snackbar.innerText = message;
    document.body.appendChild(snackbar);
    snackbar.className = "show";  
    setTimeout(function(){ snackbar.className = snackbar.className.replace("show", ""); }, 3000);

}