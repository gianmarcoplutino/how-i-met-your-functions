package com.function;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;




public class Function {
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = { HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> req,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
          try { 
            String requestData = req.getBody().orElseThrow(null);
            
            // Crea un documento PDF con i dati ottenuti
            ByteArrayOutputStream pdfStream = createPdf(requestData);
            // Salva il PDF nello storage account utilizzando la managed identity
            saveToStorage(pdfStream, context);
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
    private ByteArrayOutputStream createPdf(String data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("Dati inviati:"));
        document.add(new Paragraph(data));
        document.close();
        return outputStream;
    }
       private void saveToStorage(ByteArrayOutputStream pdfStream, ExecutionContext context) throws Exception {
        // Usa la managed identity per autenticarsi
        DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();
        BlobServiceClientBuilder blobServiceClientBuilder = new BlobServiceClientBuilder()
        .endpoint("https://xmastteststorage.blob.core.windows.net/")
        .credential(credentialBuilder.build());
        // Ottiene riferimento al contenitore e al blob dello storage account
        BlobClient blobClient = blobServiceClientBuilder.buildClient().getBlobContainerClient("testxmas").getBlobClient("pdfxmas.pdf");
          try (InputStream pdfInputStream = new ByteArrayInputStream(pdfStream.toByteArray())) {
                blobClient.upload(pdfInputStream, pdfStream.size(), true);
            } catch (Exception e) {
                context.getLogger().severe("Errore durante il caricamento del PDF nello storage: " + e.getMessage());
            }
    }
}


