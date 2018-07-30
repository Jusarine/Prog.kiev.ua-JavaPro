package com.spring.mvc;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
@SessionAttributes("file_names")
public class MyController {

    private static final String ARCHIVE_EXPANSION = ".zip";

    private Map<String, byte[]> files = new HashMap<>();
    private Map<String, byte[]> archives = new HashMap<>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping("favicon.ico")
    public void returnNoFavicon() {
    }

    @RequestMapping(value = "/upload_file", method = RequestMethod.POST)
    public String onAddFile(@RequestParam MultipartFile file) {
        if (file.isEmpty())
            throw new FileErrorException();
        try{
            files.put(file.getOriginalFilename(), file.getBytes());
            return "index";

        } catch (IOException e) {
            throw new FileErrorException();
        }
    }

    @RequestMapping(value = "/upload_archive", method = RequestMethod.POST)
    public String onAddArchive(@RequestParam MultipartFile file) {
        if (file.isEmpty())
            throw new FileErrorException();
        try{
            archives.put(file.getOriginalFilename(), file.getBytes());
            return "index";

        } catch (IOException e) {
            throw new FileErrorException();
        }
    }

    @RequestMapping("/files")
    public ModelAndView onFiles(){
        return new ModelAndView("files", "file_names", files.keySet());
    }

    @RequestMapping("/archives")
    public ModelAndView onArchives(){
        return new ModelAndView("archives", "archive_names", archives.keySet());
    }

    @RequestMapping(value = "/archive", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> onArchive(
            @RequestParam(value = "checked_files") String[] checkedFiles,
            @RequestParam(value = "archive_name") String archiveName){

        byte[] archive = filesToArchive(checkedFiles);
        archives.put(archiveName + ARCHIVE_EXPANSION, archive);
        return download(archiveName + ARCHIVE_EXPANSION, archive);
    }

    @RequestMapping(value = "/extract/{name:.+}", method = RequestMethod.GET)
    public ModelAndView onExtract(@PathVariable String name){
        return new ModelAndView("extracted_files", "archive_files", archiveToFiles(name));
    }

    @RequestMapping(value = "/download_archive/{name:.+}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> onDownloadArchive(@PathVariable String name){
        return download(name, archives.get(name));
    }

    @RequestMapping(value = "/download_file/{name:.+}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> onDownloadFile(@PathVariable String name){
        return download(name, files.get(name));
    }

    private ResponseEntity<ByteArrayResource> download(String fileName, byte[] data) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    private byte[] filesToArchive(String[] fileNames)  {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zout = new ZipOutputStream(baos)){

            for (String fileName : fileNames) {
                ZipEntry entry = new ZipEntry(fileName);
                zout.putNextEntry(entry);
                byte[] buffer = files.get(fileName);
                zout.write(buffer);
            }
        } catch (IOException e) {
            throw new FileErrorException();
        }
        return baos.toByteArray();
    }

    private List<String> archiveToFiles(String archiveName){

        List<String> fileNames = new LinkedList<>();
        try(ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(archives.get(archiveName)))){

            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                String entryName = entry.getName();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    baos.write(c);
                }
                fileNames.add(entryName);
                files.put(entryName, baos.toByteArray());
            }
        } catch (IOException e) {
            throw new FileErrorException();
        }
        return fileNames;
    }
}
