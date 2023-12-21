package com.function.service;


import com.microsoft.azure.functions.ExecutionContext;

import java.io.ByteArrayOutputStream;

public interface HimyfService {

    ByteArrayOutputStream createPdf(String s);

    void saveToStorage(ByteArrayOutputStream pdfStream, String fileId, ExecutionContext context);

    byte[] getPdfFromStorage(String fileId, ExecutionContext context);
}
