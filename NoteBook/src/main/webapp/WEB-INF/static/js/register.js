function form_submit(){
    let otp_field = document.getElementsByClassName("otp")[0];
    let email = document.getElementsByClassName("email")[0].value;
    let full_name = document.getElementsByClassName("full_name")[0].value;
    if(full_name.length == 0 ){
        toast("Name Can Not be Empty");
         return false;
    } 
    if(!isValidEmail(email)){
        toast("Invalid Email Address");
        return false;
    }


    if(otp_field.style.display == 'none' ){
        let p = fetch(`/api/user/otp_generate?email=${email}`);
        p.then((response)=>{
            if(response.ok){
                toast("Otp is Sent Successfully...");
                otp_field.style.display="block";
            }
            else{
                toast("Otp is Not Sent");
            }
        },(error)=>{
            toast("Error While Generating Otp");
        });
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
                toast("Password Should Consist More Than 5 Characters");
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

    return flag;
}

