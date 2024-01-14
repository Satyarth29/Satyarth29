/*
package com.example.OrderService;

import com.example.OrderService.dto.InventoryResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

public class InventoryMocks {


    public static void setupMockBooksResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/api/inventory"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody(
                                copyToString(
                                        InventoryMocks.class.getClassLoader().getResourceAsStream("payload/inventoryResponse.json"),
                                        defaultCharset()))));
    }
}
*/
