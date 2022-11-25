package main;

import baluni.filestorage.MyFileStorage;
import baluni.filestorage.StorageConfig;
import baluni.filestorage.StorageManager;
import baluni.model.Fajl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.io.Files;
import com.sun.source.tree.TryTree;
import main.comparators.CreationDateComparator;
import main.comparators.DateModifiedComparator;
import main.comparators.NameComparator;


import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


public class GoogleFileStorage extends MyFileStorage{

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY,
                    DriveScopes.DRIVE_METADATA,
                    DriveScopes.DRIVE,
                    DriveScopes.DRIVE_FILE,
                    DriveScopes.DRIVE_APPDATA);

    private static final String CREDENTIALS_FILE_PATH = "/config_moj.json";

    private static Drive service=null;
    public static long usedSpace = 0;

    static {
        StorageManager.initStorage(new GoogleFileStorage());
        try {
            service=init();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private static  Map<String,String> extensions = new HashMap<>();
    public static Map<String, String> pathsMap = new HashMap<>();

    public void loadExtensions() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/mimeTypes.json")));

//        java.io.File fileObj=new java.io.File("src/main/resources/mimeTypes.json");
        try{
            extensions=mapper.readValue(br, new TypeReference<Map<String, String>>(){});
           // System.out.println("Extension name " +extensions.get(""));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            br.close();
        }
    }


    public static Drive init() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = Autentifikacija.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }


    @Override
    public boolean createStorage(String storagePath) {
        super.setSotragePath(storagePath);
        try {
            loadExtensions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = new ArrayList<>();
        String pageToken;
        do {
            FileList result = null;
            try{
                /* Pretrazi prosledjeni path i vidi da li postoji fajl
                    koji u svom parentu ima taj path i proveri da li je
                    taj fajl config.json
                * */
                result = service.files().list()
                        .setQ("'"+storagePath+"' in parents and not trashed and name='config.json'")
                        .setFields("files/*")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Ukoliko je result list prazan, treba da generisem config
            if(result == null){
//                System.out.println("Treba ja da napravim config");
                String configFilePath = System.getProperty("user.home") + "\\Desktop\\def.json";
                super.writeDefaultConfig(configFilePath);
                List<Fajl> fajl = new ArrayList<>();
                fajl.add(new Fajl("config","json",configFilePath));
                this.fileUpload(storagePath, fajl);
                return true;
            }
            files.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        }while(pageToken != null);

        /* Proveravam da li sam uspeo da pronadjem neke fajlove
            unutar storagePath-a. Ako nisam, treba da napravim
            konfig. Pozivam natklasu MyFileStorage i unutar nje
            pozivam metodu writeDefaultConfig, na Desktop pod nazivom
            def.json. Nakon toga pripremam pomocnu listu fajlova koju cu
            da upload-ujem na google drive unutar storagePath-a.Ovaj fajl
            cu nazvati config.json zbog buducih pretraga.

            Vracam true jer sam uspeo da napravim storage.
        * */
        if(files == null || files.isEmpty()){
            System.out.println("Treba ja da napravim config");
            String configFilePath = System.getProperty("user.home") + "\\Desktop\\def.json";
            super.writeDefaultConfig(configFilePath);
            List<Fajl> fajl = new ArrayList<>();
            fajl.add(new Fajl("config","json",configFilePath));
            this.fileUpload(storagePath, fajl);
            return true;
        }

        /* Ukoliko unutar storagePath-a postoje neki fajlovi
            i postoji fajl config.json za koji je storagePath
            parent ja onda zelim da download-ujem taj fajl na
            desktop, lokalno da procitam taj fajl i da uz
            pomoc ObjectMappera iz biblioteke jackson napravim
            StorageConfig objekat koji cu da postavim za StorageConfig.
        * */

        download(files.get(0).getId(), System.getProperty("user.home") + "\\Desktop");
        java.io.File file = new java.io.File(String.valueOf((Paths.get(System.getProperty("user.home") + "\\Desktop" + "\\" + files.get(0).getName()))));

        if(file == null) {
            System.out.println("Nesto je poslo naopako");
            return false;
        }

        if(file.exists()){
            ObjectMapper mapper = new ObjectMapper();
            StorageConfig config = null;
            try {
                config = mapper.readValue(file, StorageConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[-] createStorage: ObjectMapper error");
            }
            this.setStorageConfig(config);

            return true;
        }

        /*
        *  Ako fajl koji sam download-ovao ne postoji na lokalu,
        *  pozivam natklasu MyFileStorage i unutar nje writeDefaulConfig
        *  metodu koja ce da generise default config.json fajl. Pripremim
        *  pomocnu listu za upload fajla koji cu da nazovem config.json
        *  Upload fajla na storagePath, return true jer sam uspeo da napravim
        *  storage.
        * */

        super.writeDefaultConfig(System.getProperty("user.home") + "\\Desktop\\def.json");
        List<Fajl> fajl = new ArrayList<>();
        fajl.add(new Fajl("config","json","C:\\Users\\Vid\\Desktop\\def.json"));
        this.fileUpload(storagePath, fajl);

        return true;
    }

    @Override
    public boolean createDirectories(String folderId, String creationPattern) {
        String[] data = new String[100];
        String[] range = new String[100];
        String fileName = "";

        if(folderId.isEmpty() || folderId.equals("."))
            folderId = this.getSotragePath();

        if(creationPattern.contains("{")){
            data = creationPattern.split("\\{");
            if(data[1].contains(".."))
                range = data[1].split("\\.\\.");
            if(data[1].contains("->"))
                range = data[1].split("->");
        }

        if(creationPattern.contains("[")){
            data = creationPattern.split("\\[");
            range = data[1].split(":");
        }

        fileName = data[0];

        int start_idx = -1;
        int end_idx = -1;

        try{
            start_idx = Integer.parseInt(range[0]);
            end_idx = Integer.parseInt(range[1].substring(0, range[1].length()-1));
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

        if(start_idx < 0 || end_idx < 0){
            System.out.println("Please provide positive values for range");
            return false;
        }else{
            System.out.println(start_idx + " " + end_idx);
        }

        if(start_idx > end_idx){
            for(int i=start_idx;i>=end_idx;i--){
                if(this.getStorageConfig().getFoldersWithCapacity().containsKey(folderId)){
                    int destContentSize = this.listFilesInDir(folderId).size() + this.listDirectoriesForDir(folderId).size();
                    int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(folderId);
                    if(destContentSize >= allowdContentSize) {
                        System.out.println("Directory is full");
                        return false;
                    }
                }

                createDir(folderId, fileName+i);
            }
        }else{
            for(int i = start_idx; i <= end_idx; i++){
                if(this.getStorageConfig().getFoldersWithCapacity().containsKey(folderId)){
                    int destContentSize = this.listFilesInDir(folderId).size()  + this.listDirectoriesForDir(folderId).size();
                    int allowdContentSize = this.getStorageConfig().getFoldersWithCapacity().get(folderId);
                    if(destContentSize >= allowdContentSize) {
                        System.out.println("Directory is full");
                        return false;
                    }
                }

                createDir(folderId, fileName+i);
            }
        }
        return true;
    }

    @Override
    public boolean createDir(String folderId, String folderName){
        File fileMeta = new File();
        fileMeta.setName(folderName);
        fileMeta.setMimeType("application/vnd.google-apps.folder");

        if(folderId.isEmpty() || folderId.equals("."))
            folderId = this.getSotragePath();

        fileMeta.setParents(Collections.singletonList(folderId));

        File file = null;

        try{
            file = service
                    .files()
                    .create(fileMeta)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file == null)
            return false;
        return true;
    }

    @Override
    public boolean createDirectory(String destinationId, String folderName, int capacity) {
        File fileMeta = new File();
        fileMeta.setName(folderName);
        fileMeta.setMimeType("application/vnd.google-apps.folder");

        if(destinationId.isEmpty() || destinationId.equals("."))
            destinationId = this.getSotragePath();

        fileMeta.setParents(Collections.singletonList(destinationId));

        File file = null;

        try{
            file = service
                    .files()
                    .create(fileMeta)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file == null)
            return false;

        System.out.println(file.getId());

        this.getStorageConfig().getFoldersWithCapacity().put(file.getId(),capacity);
        return true;
    }

    @Override
    public boolean createFile(String path, String fileName) {
        return false;
    }

    @Override
    public void buildPath(String s) {
        return;
    }

    @Override
    public void fileUpload(String destination, List<Fajl> list) {
        if(destination.isEmpty() || destination.equals("."))
            destination = this.getSotragePath();

        if(this.getStorageConfig().getFoldersWithCapacity().get(destination) != null){
            int contentSize = this.listFilesInDir(destination).size() + this.listDirectoriesForDir(destination).size();
            int allowedSize = this.getStorageConfig().getFoldersWithCapacity().get(destination);

            if(contentSize >= allowedSize){
                System.out.println("Limit for " + destination + " has been reached. Cannot upload file");
                return;
            }
        }

        for(Fajl fajl:list){
            if(this.getStorageConfig().getForbiddenExtensions().contains(fajl.getExtension())){
                continue;
            }

            if(usedSpace + fajl.getFileSize() > this.getStorageConfig().getDefaultStorageSize()){
                continue;
            }

            usedSpace += fajl.getFileSize();

            System.out.println("Iskoriscen prostor " + usedSpace);

            File fileMetaData=new File();
            fileMetaData.setName(fajl.getFileName()+"."+fajl.getExtension());
            fileMetaData.setMimeType(extensions.get(fajl.getExtension()));
            fileMetaData.setParents(Collections.singletonList(destination));

            java.io.File filePath =new java.io.File(fajl.getPath());
            FileContent mediaContent=new FileContent(extensions.get(fajl.getExtension()),filePath);
            try{
                File file=service.files().create(fileMetaData,mediaContent)
                        .setFields("id").execute();
                System.out.println("FILE id: "+ file.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //skips trash
    @Override
    public void deleteFiles(List<Fajl> list) {
        try{
            for(Fajl fajl:list) {
                service.files().delete(fajl.getPath()).execute();
                usedSpace -= fajl.getFileSize();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDirectories(List<Fajl> list) {
        deleteFiles(list);
    }

    @Override
    public void moveFiles(String fileId, String destination) {
        try {
            File file=service.files().get(fileId).setFields("name, id, parents, mimeType").execute();
            StringBuilder prevParents=new StringBuilder();

            if(fileId.isEmpty() || fileId.equals("."))
                fileId = this.getSotragePath();

            // Test move
            if(this.getStorageConfig().getFoldersWithCapacity().get(destination) != null){
                int contentsSize = this.listFilesInDir(destination).size() + this.listDirectoriesForDir(destination).size();
                int allowedSize = this.getStorageConfig().getFoldersWithCapacity().get(destination);

                if(contentsSize >= allowedSize){
                    System.out.println("Limit for " + destination + " has been reached. Cannot move files");
                    return;
                }
            }

            String ext = "";

            for(String key : extensions.keySet()){
                if(extensions.get(key).equals(file.getMimeType())){
                    ext = key;
                }
            }

            if(this.getStorageConfig().getForbiddenExtensions().contains(ext)){
                System.out.println("This extension is forbidden");
                return;
            }

            for(String parent: file.getParents()){
                prevParents.append(parent);
                prevParents.append(",");
            }

            file=service.files().update(fileId,null)
                    .setAddParents(destination)
                    .setRemoveParents(prevParents.toString())
                    .setFields("id, parents")
                    .execute();

            if(file!=null) {
                System.out.println("uspeh");
                return;
            }else {
                System.out.println("neuspeh");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[-] moveFiles: Cannot move given files to desired location");
        }

    }

    @Override
    public void moveFile(String s, String s1) {
        return;
    }

    @Override
    public void download(String resourceToDownload, String destination) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream outputLocation = null;
        try {
            File file = service.files().get(resourceToDownload).execute();

            service.files().get(resourceToDownload).executeMediaAndDownloadTo(outputStream);

            String fileName = "";
            String ext = "";

            if (file.getName().contains(".")){
                fileName = file.getName();
            }else {
                System.out.println(file.getMimeType());
                for(String key : extensions.keySet())
                    if(extensions.get(key).equals(file.getMimeType())) {
                        ext = key;
                        break;
                    }
                System.out.println(ext);
                fileName = file.getName() + "." + ext;
            }

            System.out.println(fileName);

            destination = destination + "\\" + fileName;

            outputLocation = new FileOutputStream(destination);

            ByteArrayOutputStream bos = outputStream;
            bos.writeTo(outputLocation);
        }catch (GoogleJsonResponseException e){
            System.err.print("Unable to export file: " + e.getDetails());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean rename(String fileId, String newFileName) {

       try{
           File file=new File();

           if(newFileName.contains(".")){
               if(this.getStorageConfig().getForbiddenExtensions().contains(newFileName.substring(newFileName.lastIndexOf(".")+1,newFileName.length()))){
                   System.out.println("[-] rename: Cannot rename file because provided name contains forbidden extension");
                   return false;
               }
           }

           file.setName(newFileName);
           Drive.Files.Update update=service.files().update(fileId,file);
           File newFile=update.execute();
           if(newFile!=null)
                return true;
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("[-] rename: Cannot rename file " + fileId + " to " + newFileName);
       }
        return false;
    }

    @Override
    public List<Fajl> listFilesInDir(String path) {
        List<File>files=new ArrayList<>();
        String pageToken=null;
        // String driveName="";

        if(path.isEmpty() || path.equals("."))
            path = this.getSotragePath();

        String query="'"+path+"' in parents and trashed=false";

        do{
            FileList result=null;
            try{
                result = service.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/*")
                        .setPageToken(pageToken)
                        .execute();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("[-] listFilesInDir: Google Drive API error");
            }
            files.addAll(result.getFiles());
            pageToken=result.getNextPageToken();
        }while(pageToken!=null);

        List<Fajl> resultList = new ArrayList<>();

        for(File file:files) {
            String ext = "";
            String filename = "";
            /**Odavde mogu da uzmem ekstenziju ovako, ne moram da mapiram**/
//            System.out.println("fileName -> " + file.getFileExtension());

            if(file.getName().contains(".")){
                filename = file.getName().substring(0, file.getName().indexOf('.'));
                ext = file.getFileExtension();
            }else {
                for (String key : extensions.keySet()) {
                    if (extensions.get(key).equals(file.getMimeType())) {
                        ext = key;
                        break;
                    }
                }
                filename = file.getName();
            }

            if(ext.equalsIgnoreCase("folder"))
                continue;
//
//            String filename = "";

//            if(file.getName().contains("."))
//                filename = file.getName().substring(0, file.getName().indexOf('.'));
//            else
//                filename = file.getName();

          //  System.out.println(filename + " " + file.getId() + " " + file.getMimeType() + " " + ext + " " + file.getCreatedTime());
            LocalDate creationTime = toLocalDate(file.getCreatedTime());
            LocalDate modificationTime = toLocalDate(file.getModifiedTime());
            long fileSize = 0;

            try{
                fileSize = file.getSize();
            }catch (Exception e){
                System.out.println("File has 0 bytes");
            }

            resultList.add(new Fajl(filename,ext,file.getId(),creationTime,modificationTime,fileSize));
        }
        return resultList;
    }

    @Override
    public List<Fajl> listFilesInSubDir(String path) {
        List<Fajl> res = new ArrayList<>(this.listFilesInDir(path));
        List<Fajl> dirs = new ArrayList<>(this.listDirectoriesForDir(path));

        while (!dirs.isEmpty()){
            Fajl dir = dirs.get(0);
            dirs.addAll(this.listDirectoriesForDir(dir.getPath()));
            res.addAll(this.listFilesInDir(dir.getPath()));
            dirs.remove(0);
        }

        return res;
    }

    @Override
    public List<Fajl> listFiles(String s) {
        return listFilesInDir(s);
    }

    //metoda koja lista root
    @Override
    public List<Fajl> listFilesForExtension(String extension) {
        List<File> files = new ArrayList<>();
        List<Fajl> fajlovi = new ArrayList<>();

        String pageToken = null;
        String driveExtension = extensions.get(extension);

        String query = "'root' in parents and mimeType='" + driveExtension + "'";

        do{
            FileList result = null;
            try{
                result = service
                        .files()
                        .list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/*")
                        .setPageToken(pageToken)
                        .execute();
            }catch (IOException e){
                e.printStackTrace();
            }

            files.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        }while(pageToken != null);

        for(File file : files){
            String name = "";
            String ext = "";

            if(file.getName().contains(".")) {
                name = file.getName().substring(0, file.getName().lastIndexOf("."));
                ext = file.getFileExtension();
            }else {
                name = file.getName();

//            String ext = "";

                for (String key : extensions.keySet()) {
                    if (extensions.get(key).equals(file.getMimeType())) {
                        ext = key;
                        break;
                    }
                }
            }
            LocalDate dateCreated = toLocalDate(file.getCreatedTime());
            LocalDate dateModified = toLocalDate(file.getModifiedTime());
            fajlovi.add(new Fajl(name, ext, this.getSotragePath(), dateCreated, dateModified, file.getSize()));
        }

        return fajlovi;
    }

    @Override
    public List<Fajl> listFilesForExtension(String id, String extension) {
        List<File> files = new ArrayList<>();
        List<Fajl> fajlovi = new ArrayList<>();

        String pageToken = null;
        String driveExtension = extensions.get(extension);

        if (id.isEmpty() || id.equals("."))
            id = "root";

        String query = "'" + id + "' in parents and mimeType='" + driveExtension + "'";

        do{
            FileList result = null;
            try{
                result = service
                        .files()
                        .list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/*")
                        .setPageToken(pageToken)
                        .execute();
            }catch (IOException e){
                e.printStackTrace();
            }

            files.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        }while(pageToken != null);

        for(File file : files){
            String name = "";
            String ext = "";

            if(file.getName().contains(".")) {
                name = file.getName().substring(0, file.getName().lastIndexOf("."));
                ext = file.getFileExtension();
            }else {
                name = file.getName();

//            String ext = "";

                for (String key : extensions.keySet()) {
                    if (extensions.get(key).equals(file.getMimeType())) {
                        ext = key;
                        break;
                    }
                }
            }
            LocalDate dateCreated = toLocalDate(file.getCreatedTime());
            LocalDate dateModified = toLocalDate(file.getModifiedTime());
            fajlovi.add(new Fajl(name, ext, id, dateCreated, dateModified, file.getSize()));
        }

        return fajlovi;
    }

    @Override
    public List<Fajl> listFilesForName(String fileName) {

        List<File>files=new ArrayList<>();
        String pageToken=null;
       // String driveName="";
        String query="('root' in parents and mimeType!='application/vnd.google-apps.folder' and mimeType!='application/vnd.google-apps.shortcut') and name contains '"+ fileName+"'";

        do{
            FileList result=null;
            try{
                result=service.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/name, files/id")
                        .setPageToken(pageToken)
                        .execute();

            }catch (Exception e){
                e.printStackTrace();
            }
            files.addAll(result.getFiles());
            pageToken=result.getNextPageToken();
        }while(pageToken!=null);

        for(File file:files)
            System.out.println(file.getName());

        return null;
    }

    @Override
    public List<Fajl> listFilesForName(String id, String fileName) {

        List<File> files=new ArrayList<>();
        List<Fajl> fajlovi = new ArrayList<>();

        String pageToken=null;
        String query="('" + id + "' in parents and mimeType!='application/vnd.google-apps.folder' and mimeType!='application/vnd.google-apps.shortcut') and name contains '"+ fileName+"'";

        do{
            FileList result=null;
            try{
                result=service.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/*")
                        .setPageToken(pageToken)
                        .execute();

            }catch (Exception e){
                e.printStackTrace();
            }
            files.addAll(result.getFiles());
            pageToken=result.getNextPageToken();
        }while(pageToken!=null);

        for(File file:files) {
//            String fName = file.getName().substring(0, file.getName().indexOf('.'));
            String fName = "";
            String ext = "";

            if(file.getName().contains(".")){
                fName = file.getName().substring(0, file.getName().indexOf('.'));
                ext = file.getFileExtension();
            }else {
                fName = file.getName();
                for (String key : extensions.keySet()) {
                    if (extensions.get(key).equals(file.getMimeType())) {
                        ext = key;
                        break;
                    }
                }
            }

            if(file.getFileExtension() == null)
                ext = "folder";
            else
                ext = file.getFileExtension();

            LocalDate dateCreated = toLocalDate(file.getCreatedTime());
            LocalDate dateModified = toLocalDate(file.getModifiedTime());
            long size = file.getSize();

            fajlovi.add(new Fajl(fName, ext, id, dateCreated, dateModified, size));
        }

        return fajlovi;
    }

    @Override
    public boolean listDirForNames(String path, List<String> fileNames) {
        List<String> fileNamesList=new ArrayList<>();

        List<Fajl> listaSvihFajlova=listFilesInSubDir(path);

        for(Fajl fajl:listaSvihFajlova){
            fileNamesList.add(fajl.getFileName());
        }

        if(fileNamesList.containsAll(fileNames))
              return true;

        return false;
    }

    public List<Fajl> listDirectoriesForDir(String path){
        List<File> files=new ArrayList<>();
        String pageToken=null;

        if(path.isEmpty() || path.equals("."))
            path = this.getSotragePath();

        String query="'"+path+"' in parents and trashed=false";
        do{
            FileList result=null;
            try {
                result=service.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken ,files/*")
                        .setPageToken(pageToken)
                        .execute();
            }catch (Exception e){
                e.printStackTrace();
            }
            files.addAll(result.getFiles());
            pageToken=result.getNextPageToken();
        }while (pageToken!=null);
        List<Fajl> resultList=new ArrayList<>();
        for(File file:files){
            String ext="";
            for(String key:extensions.keySet()){
                if(extensions.get(key).equals(file.getMimeType())){
                    ext=key;
                    break;
                }
            }

            if(!(ext.equalsIgnoreCase("folder")))
                continue;
            DateTime createdTime=file.getCreatedTime();
            LocalDate creationTime=toLocalDate(createdTime);
            DateTime modifyTime=file.getModifiedTime();
            LocalDate modificationTime=toLocalDate(modifyTime);
            String fileName="";
            long fileSize=0;
            if(ext.equals("folder")){
                fileName=file.getName();
                fileSize=0;
            }else{
                fileName=file.getName().substring(0,file.getName().indexOf('.'));
                fileSize=file.getSize();
            }
            resultList.add(new Fajl(fileName,ext,file.getId(),creationTime,modificationTime,fileSize));
        }

        return resultList;
    }

    @Override
    public Fajl findDirectoryOfFile(String fileName) {
        List<File> files = new ArrayList<>();
        List<Fajl> contents = this.listFilesInSubDir(".");

        String pageToken = null;
        String query = "name = '" + fileName + "'";

        do{
            FileList result = null;
            try {
                result = service.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("nextPageToken, files/*")
                        .setPageToken(pageToken)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            files.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        }while(pageToken != null);

        File target = files.get(0);

        return new Fajl(target.getParents().get(0),"folder",target.getParents().get(0),null,null,0);
    }

    @Override
    public List<Fajl> sort(List<Fajl> fileList,  boolean byName, boolean creationDate, boolean dateModified, boolean asc) {
        if(byName){
            Collections.sort(fileList,new NameComparator(asc));
        }

        if(creationDate){
            Collections.sort(fileList,new CreationDateComparator(asc));
        }

        if(dateModified){
            Collections.sort(fileList,new DateModifiedComparator(asc));
        }

        return fileList;
    }

    @Override
    public List<Fajl> listFileByDate(String date, String id) {
        LocalDate endDate = LocalDate.parse(date);

        List<Fajl> dirContent = this.listFilesInSubDir(id);
        List<Fajl> resultList = new ArrayList<>();

        for(Fajl fajl : dirContent){
            if(fajl.getCreationDate().equals(endDate) || fajl.getModificationDate().equals(endDate)){
                resultList.add(fajl);
            }
        }

        return resultList;
    }

    @Override
    public List<Fajl> listFilesBetweenDates(String startDate, String endDate, String id) {
        List<Fajl> contentsOfDir = this.listFilesInSubDir(id);
        List<Fajl> resultList = new ArrayList<>();

        LocalDate startParsed = LocalDate.parse(startDate);
        LocalDate endParsed = LocalDate.parse(endDate);

        for(Fajl fajl : contentsOfDir){
            if(fajl.getCreationDate().isBefore(endParsed) && fajl.getCreationDate().isAfter(startParsed)
                    || (fajl.getModificationDate().isAfter(startParsed) && fajl.getModificationDate().isBefore(endParsed))
                    || (fajl.getModificationDate().equals(endParsed) || fajl.getModificationDate().equals(startParsed))
                    || (fajl.getCreationDate().equals(endParsed) || fajl.getCreationDate().equals(startParsed))){
                resultList.add(fajl);
            }
        }

        return resultList;
    }

    @Override
    public List<Fajl> filterData(List<Fajl> fileList,boolean byPath,boolean byName,boolean bySize,boolean byCreationDate
            ,boolean byModificationDate, boolean byExtension) {

        List<Fajl> outputList=new ArrayList<>();
        for(Fajl fajl:fileList){
            Fajl newFile=new Fajl("","","",null,null,0);
            if(byPath){
                newFile.setPath(fajl.getPath());
            }
            if(byName){
                newFile.setFileName(fajl.getFileName());
            }

            if(bySize){
                newFile.setFileSize(fajl.getFileSize());
            }

            if(byCreationDate){
                newFile.setCreationDate(fajl.getCreationDate());
            }

            if(byModificationDate){
                newFile.setModificationDate(fajl.getModificationDate());
            }

            if(byExtension){
                newFile.setExtension(fajl.getExtension());
            }

            outputList.add(newFile);
        }

        return outputList;
    }

    private static LocalDate toLocalDate(DateTime dt){
        try{
            DateTimeFormatter f=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return LocalDate.parse(dt.toStringRfc3339(),f);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("[-] toLocalDate: Cannot convert DateTime to LocalDate");
            return null;
        }
    }

    @Override
    public void saveStorageConfig(String rootPath) {
        List<Fajl> filesInRoot = this.listFilesInDir(rootPath);

        String fileId = "";

        for(Fajl file : filesInRoot){
            if(file.getFileName().equals("config")){
                fileId = file.getPath();
                break;
            }
        }

        this.download(fileId, "C:\\Users\\Vid\\Desktop\\");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Paths.get("C:\\Users\\Vid\\Desktop\\config.json").toFile(), this.getStorageConfig());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        this.deleteFiles(List.of(new Fajl("config","json",fileId)));

        this.fileUpload(rootPath, List.of(new Fajl("config","json","C:\\Users\\Vid\\Desktop\\config.json")));
    }
}
