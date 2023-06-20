


function toast(message) {
  
    if(performance.navigation.type == 2) return;

    let snackbar = document.createElement("div");
    snackbar.id = "snackbar";
    snackbar.innerText = message;
    document.body.appendChild(snackbar);
    snackbar.className = "show_toast";  
    setTimeout(function(){ snackbar.className = snackbar.className.replace("show_toast", ""); }, 3000);

}