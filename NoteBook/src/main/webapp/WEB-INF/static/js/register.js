function form_submit(){
    let otp_field = document.getElementsByClassName("otp")[0];
    let email = document.getElementsByClassName("email")[0].value;
    if(!isValidEmail(email)){
        return false;
    }


    if(otp_field.style.display == 'none' ){
        let p = fetch(`/api/user/otp_generate?email=${email}`);
        p.then((response)=>{
            if(response.ok){
                toast("Otp is sent Successfully...");
            }
            else{
                toast("Otp is not sent");
            }
        },(error)=>{
            toast("Error while generating Otp");
        });
        otp_field.style.display="block";
        return false;
    }
    else{

        let password = document.getElementsByClassName("password")[0].value;
        let otp= document.getElementsByClassName("otp_input")[0].value;
        if(otp.length !=6 ){
            toast("Invalid Otp");
            return false;
        }
        else {

            if(password.length <6 ){
                toast("Password should consist more than 5 characters");
                return false;
            }
            else {
                return true;
            }

        }
    }

}

function isValidEmail(email){

    let flag = false;

    for(let i =0;i<email.length;i++){
        if(email[i]=='@'){

            if(!( i > 0 && i < email.length - 1 ) || flag) return false;

            flag = true;
        }

    }

    return true;
}

