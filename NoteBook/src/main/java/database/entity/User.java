package database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {
    @Id
    private String email;

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @Column
    @ColumnDefault("0")
    private boolean oauth2 = false;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    List<Note> notes;


    @JsonBackReference
    @OneToMany(mappedBy = "from",fetch = FetchType.LAZY)
    List<Chat> chatsAsSender;



    @JsonBackReference
    @OneToMany(mappedBy = "to",fetch=FetchType.LAZY)
    List<Chat> chatAsReceiver;


    @JsonBackReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    List<Resource> resources;

    public boolean isOauth2() {
        return oauth2;
    }


    @JsonIgnore
    public void setOauth2(boolean oauth2) {
        this.oauth2 = oauth2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Chat> getChatsAsSender() {
        return chatsAsSender;
    }

    public void setChatsAsSender(List<Chat> chatsAsSender) {
        this.chatsAsSender = chatsAsSender;
    }

    public List<Chat> getChatAsReceiver() {
        return chatAsReceiver;
    }

    public void setChatAsReceiver(List<Chat> chatAsReceiver) {
        this.chatAsReceiver = chatAsReceiver;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
