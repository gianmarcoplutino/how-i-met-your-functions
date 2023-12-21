package com.function.service;


import com.microsoft.azure.functions.ExecutionContext;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

public interface HimyfService {

    ByteArrayOutputStream createPdf(String s);

    void saveToStorage(ByteArrayOutputStream pdfStream, String fileId, ExecutionContext context);

    //Optional<Character> getCharacterFromMongo(String id);
}
