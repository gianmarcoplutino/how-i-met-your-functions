package com.function.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.function.entity.Character;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.microsoft.azure.functions.ExecutionContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

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
    public void saveToStorage(ByteArrayOutputStream pdfStream, ExecutionContext context){
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
