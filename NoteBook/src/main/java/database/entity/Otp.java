package database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Otp {

    @Id
    private String email;

    @Column
    private String otp;

    private Timestamp expire;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getExpire() {
        return expire;
    }

    public void setExpire(Timestamp expire) {
        this.expire = expire;
    }
}
