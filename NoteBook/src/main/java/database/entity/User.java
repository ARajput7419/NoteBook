package database.entity;

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

    @Column(nullable = false)
    private String password;

    @Transient
    @OneToMany(mappedBy = "user")
    List<Note> notes;

    @Transient
    @OneToMany(mappedBy = "from",fetch = FetchType.LAZY)
    List<Chat> chatsAsSender;

    @Transient
    @OneToMany(mappedBy = "to",fetch=FetchType.LAZY)
    List<Chat> chatAsReceiver;

    @Transient
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    List<Resource> resources;

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

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

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
