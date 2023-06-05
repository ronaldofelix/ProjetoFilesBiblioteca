package projeto.multimidia.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import projeto.multimidia.service.S3Util;

@Controller
public class MainController {

    @GetMapping("/")
    public String exibirPaginaInicial() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadArquivo(String description, @RequestParam("file") MultipartFile multipart,
            Model model) {
        String file = multipart.getOriginalFilename();

        String message = "";
        try {
            S3Util.uploadArquivo(file, multipart.getInputStream());
            message = "Arquivo enviado com sucesso!";
        } catch (Exception e) {
            message = "Erro: não foi possível realizar o upload. " + e.getMessage();
        }
        model.addAttribute("message", message);
        return "message";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadArquivo(@RequestParam("file") String fileName) {
        try {
            InputStreamResource resource = S3Util.downloadArquivo(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar-arquivos")
    public String listarArquivos(Model model) {
        List<String> arquivos = S3Util.listarArquivos();
        model.addAttribute("arquivos", arquivos);
        return "index";
    }

    @GetMapping("/sobre")
    public String exibirPaginaSobre() {
        return "sobre";
    }

    @GetMapping("/contato")
    public String exibirPaginaContato() {
        return "contato";
    }
}
