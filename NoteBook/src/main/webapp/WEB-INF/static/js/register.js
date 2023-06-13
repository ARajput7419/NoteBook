function form_submit(){
    let otp_field = document.getElementsByClassName("otp")[0];
    let email = document.getElementsByClassName("email")[0].value;
    if(email)
    if(otp_field.display == 'none' ){
        let p = fetch(`/api/user/otp_generate?email=${email}`);
        p.then((response)=>{
            if(response.ok){
                // trigger message otp is sent successfully ....
            }
            else{
                // trigger message otp is not sent successfully ....
            }
        });
        otp_field.style.display="block";
        return false;
    }
    else{

        // validate all fields  then send request;

        return true;
    }

}