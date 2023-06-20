
function isValidEmail(email){

    let flag = false;

    for(let i =0;i<email.length;i++){
        if(email[i]=='@'){

            if(!( i > 0 && i < email.length - 1 ) || flag) return false;

            flag = true;
        }

    }

    return flag;
}



function form_submit(){

    let email = document.getElementsByClassName("email")[0].value;
    let password = document.getElementsByClassName("password")[0].value;
    if(!isValidEmail(email)){
        toast("Invalid Email Address");
        return false;
    }
    if(password.length<6){
        toast("Password Should Consist More Than 5 Characters");
        return false;
    }
    return true;
}
