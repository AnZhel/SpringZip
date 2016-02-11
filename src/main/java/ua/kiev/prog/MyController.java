package ua.kiev.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Andzhel on 11.02.2016.
 */
@Controller
@RequestMapping("/")
public class MyController {

    public String onIndex(){
        return "index";
    }

    @RequestMapping(value="/zip", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onZip(Model model, @RequestParam MultipartFile[] files) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zous = new ZipOutputStream(os);
        zous.setLevel(Deflater.DEFAULT_COMPRESSION);
        String fileName;

        for (MultipartFile file:files) {
            fileName = file.getOriginalFilename();
            ZipEntry ze = new ZipEntry(fileName);
            zous.putNextEntry(ze);
            zous.write(file.getBytes());
            zous.closeEntry();
        }
        zous.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Ranges","bytes");
        headers.setContentLength(os.size());
        headers.add("Content-type","application /zip");
        headers.add("Content-Disposition","attachment; filename = zipped.zip");
        headers.setConnection("Keep-Alive");
        return new ResponseEntity<byte[]>(os.toByteArray(),headers, HttpStatus.OK);
    }
}
