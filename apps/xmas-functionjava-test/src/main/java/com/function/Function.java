package com.function;

import com.function.service.HimyfService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@AllArgsConstructor
public class Function {

    private final HimyfService himyfService;


    @FunctionName("pdfFunction")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req, final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        Optional<String> optBody = req.getBody();
        String fileId = req.getQueryParameters().get("fileId");
        if (optBody.isPresent() && !StringUtils.isBlank(fileId)) {
            try {
                ByteArrayOutputStream pdfStream = himyfService.createPdf(optBody.get());
                // Salva il PDF nello storage account utilizzando la managed identity
                himyfService.saveToStorage(pdfStream, fileId, context);
                return req.createResponseBuilder(HttpStatus.OK)
                        .body("PDF creato e salvato con successo.")
                        .build();

            } catch (Exception e) {
                context.getLogger().severe(e.getMessage());
                return req.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(e.getMessage())
                        .build();
            }
        }

        return req.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("")
                .build();
    }
}


