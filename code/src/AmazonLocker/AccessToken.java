import lombok.Getter;

import java.time.Instant;

public class AccessToken {
    @Getter
    public final String code;
    public final Instant expirationTime;
    @Getter
    public final Compartment compartment;

    public AccessToken(String code, Compartment compartment, Instant expirationTime) {
        this.code = code;
        this.compartment = compartment;
        this.expirationTime = expirationTime;
    }
    public Boolean isExpired() {
        return !Instant.now().isBefore(expirationTime);
    }

}
