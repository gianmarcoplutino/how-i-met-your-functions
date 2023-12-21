package com.function.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.microsoft.azure.functions.ExecutionContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
@RequiredArgsConstructor
public class HimyfServiceDefault implements HimyfService {

    @Override
    public ByteArrayOutputStream createPdf(String data){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph(data));
            document.close();
            return outputStream;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveToStorage(ByteArrayOutputStream pdfStream, String fileId, ExecutionContext context){
        // Usa la managed identity per autenticarsi
        DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();
        BlobServiceClientBuilder blobServiceClientBuilder = new BlobServiceClientBuilder()
                .endpoint("https://xmastteststorage.blob.core.windows.net/")
                .credential(credentialBuilder.build());
        // Ottiene riferimento al contenitore e al blob dello storage account
        BlobClient blobClient = blobServiceClientBuilder
                .buildClient()
                .getBlobContainerClient("testxmas")
                .getBlobClient(fileId + ".pdf");
        try (InputStream pdfInputStream = new ByteArrayInputStream(pdfStream.toByteArray())) {
            blobClient.upload(pdfInputStream, pdfStream.size(), true);
        } catch (Exception e) {
            context.getLogger().severe("Errore durante il caricamento del PDF nello storage: " + e.getMessage());
        }
    }

    @Override
    public byte[] getPdfFromStorage(String fileId, ExecutionContext context) {
        DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();
        BlobServiceClientBuilder blobServiceClientBuilder = new BlobServiceClientBuilder()
                .endpoint("https://xmastteststorage.blob.core.windows.net/")
                .credential(credentialBuilder.build());

        // Ottiene riferimento al contenitore e al blob dello storage account
        BlobClient blobClient = blobServiceClientBuilder.buildClient()
                .getBlobContainerClient("testxmas")
                .getBlobClient(fileId + ".pdf");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            blobClient.downloadStream(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            context.getLogger().severe("Errore durante il download del PDF nello storage: " + e.getMessage());
        }

        return new byte[0];
    }


}
