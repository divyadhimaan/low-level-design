package factory;

import model.Client;

import java.util.UUID;

public class ClientFactory {

    public static Client createClient(String name, String email, String phoneNumber, String deviceToken) {
        return new Client(UUID.randomUUID().toString(), name, email, phoneNumber, deviceToken);
    }
}
