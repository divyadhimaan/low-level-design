package model;

import lombok.Getter;

@Getter
public class Client {
    private final String id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String deviceToken;

    public Client(String id, String name, String email, String phoneNumber, String deviceToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.deviceToken = deviceToken;
    }


}
