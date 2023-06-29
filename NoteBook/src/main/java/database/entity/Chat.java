package database.entity;

import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name="from_userid")
    private User from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name="to_userid")
    private User to;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp_")
    private Timestamp timestamp;

    @Column(nullable = false)
    private String message;

    @ColumnDefault("false")
    @Column(name = "read_")
    private boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
