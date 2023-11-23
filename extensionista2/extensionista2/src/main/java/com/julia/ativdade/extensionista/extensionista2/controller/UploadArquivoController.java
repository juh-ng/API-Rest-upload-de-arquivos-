package com.julia.ativdade.extensionista.extensionista2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/upload", produces = {"application/json"})
@Slf4j //para log
@CrossOrigin("*")
public class UploadArquivoController {

    private final String pathFiles;

    public UploadArquivoController(@Value("${app.path.arquivos}") String pathFiles ){

        this.pathFiles = pathFiles;
    }

    @PostMapping("/file")
    public ResponseEntity <String> saveFile (@RequestParam("file")MultipartFile file){
        log.info("recebendo o arquivo: ", file.getOriginalFilename());

        var caminho = pathFiles + UUID.randomUUID() + "." + extractExtensao(file.getOriginalFilename());

        log.info("Novo nome do arquivo" + caminho);

        try{
            Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>("{\"mensagem:\" \"arquivo carregado com sucesso\"}", HttpStatus.OK);

        }catch (Exception e){
            log.error("erro ao processar arquivo", e);
            return new ResponseEntity<>("{\"mensagem:\" \"Erro ao carregar o arquivo!\"}", HttpStatus.INTERNAL_SERVER_ERROR);



        }

    }

    private String extractExtensao(String fileName) {
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1);
    }


}
