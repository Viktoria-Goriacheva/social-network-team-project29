package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.config.ServerDb;

import java.io.IOException;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final ServerDb serverDb;


    public String getDateFromOtherAPI() throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet(serverDb.getPort() + "/users");

        HttpResponse httpresponse = httpclient.execute(httpget);

        Scanner sc = new Scanner(httpresponse.getEntity().getContent());
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        return sb.toString();
    }
}
