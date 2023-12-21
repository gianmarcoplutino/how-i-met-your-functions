package com.function;

import com.function.service.HimyfService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@AllArgsConstructor
public class Function {

    private final HimyfService himyfService;


    @FunctionName("pdfFunction")
    public void run(@HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> req, final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        Optional<String> optBody = req.getBody();
        if (optBody.isPresent()) {
            try {
                ByteArrayOutputStream pdfStream = himyfService.createPdf(optBody.get());
                // Salva il PDF nello storage account utilizzando la managed identity
                himyfService.saveToStorage(pdfStream, context);
            } catch (Exception e) {
                context.getLogger().severe(e.getMessage());
            }
        }
    }
}


